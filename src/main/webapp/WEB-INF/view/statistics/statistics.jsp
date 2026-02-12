<%@ page import="org.example.smart_parking_260219.dto.StatisticsDTO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 1. 숫자 포맷터 설정
    java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");

    // 2. 데이터 가져오기 (Null 방어 포함)
    List<StatisticsDTO> hSalesList = (List<StatisticsDTO>) request.getAttribute("hourlySales");
    List<StatisticsDTO> dSalesList = (List<StatisticsDTO>) request.getAttribute("dailySales");

    // 3. 일 총 매출 직접 계산 (hSalesList 순회)
    long daySum = 0;
    if (hSalesList != null) {
        for (StatisticsDTO dto : hSalesList) {
            daySum += dto.getValue();
        }
    }
    String formattedDayTotal = df.format(daySum);

    // 4. 월 총 매출 직접 계산 (dSalesList 순회)
    long monthSum = 0;
    if (dSalesList != null) {
        for (StatisticsDTO dto : dSalesList) {
            monthSum += dto.getValue();
        }
    }
    String formattedMonthTotal = df.format(monthSum);
%>
<html>
<head>
    <title>매출 통계 - 주차장 관리 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/statistics_style.css">
    <%-- chart.js CDN --%>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<%@ include file="../../../main/menu.jsp" %>

<div class="main-content">
    <div id="statistics" class="page">
        <h2>📊 매출 및 이용 통계</h2>

        <div class="section-card control-section">
            <form action="${pageContext.request.contextPath}/statistics/statistics" method="get" class="control-bar">
                <div class="selector-box">
                    <div class="btn-label">조회 기준일</div>
                    <input type="date" name="targetDate" value="${targetDate}" onchange="this.form.submit()">
                </div>
            </form>
        </div>

        <div class="stats-wrapper">
            <div class="section-card">
                <div class="control-bar">
                    <div class="btn-label">시간대별 통계(일별) (${targetDate})</div>
                    <div class="total-price-tag">일 총 매출: <%= formattedDayTotal %>원</div>
                </div>
                <div class="chart-area">
                    <canvas id="hourlyChart"></canvas>
                </div>
            </div>

            <div class="section-card">
                <div class="control-bar">
                    <div class="btn-label">월별 매출 현황</div>
                    <div class="total-price-tag">월 총 매출: <%= formattedMonthTotal %>원</div>
                </div>
                <div class="chart-area">
                    <canvas id="dailySalesChart"></canvas>
                </div>
            </div>

            <div class="section-card">
                <div class="control-bar">
                    <div class="btn-label">차종별 이용 비중(월별)</div>
                </div>
                <div class="chart-area">
                    <canvas id="carTypeChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/JS/menu.js"></script>
<script>
    // 1. 서버에서 넘어온 데이터를 JS 배열로 변환
    <%
        List<StatisticsDTO> hSales = (List<StatisticsDTO>) request.getAttribute("hourlySales");
        List<StatisticsDTO> hCounts = (List<StatisticsDTO>) request.getAttribute("hourlyCounts");
        List<StatisticsDTO> dSales = (List<StatisticsDTO>) request.getAttribute("dailySales");
        List<StatisticsDTO> cStats = (List<StatisticsDTO>) request.getAttribute("carTypeStats");
    %>
    /*
            req.setAttribute("targetDate", targetDate);
            req.setAttribute("hourlySales", hourlySales);
            req.setAttribute("hourlyCounts", hourlyCounts);
            req.setAttribute("dailySales", dailySales);
            req.setAttribute("carTypeStats", carTypeStats);
     */

    // 시간대별 데이터 - 0시~23시까지의 매출 및 입차 대수
    const hourlyLabels = [
        <% for(StatisticsDTO dto : hSales) { %> "<%= dto.getLabel() %>", <% } %>
    ];
    const hourlySalesData = [
        <% for(StatisticsDTO dto : hSales) { %> <%= dto.getValue() %>, <% } %>
    ];
    const hourlyCountData = [
        <% for(StatisticsDTO dto : hCounts) { %> <%= dto.getValue() %>, <% } %>
    ];

    // 일별 매출 데이터 - 해당 월의 1일~말일까지의 일간 합계
    const dailyLabels = [
        <% for(StatisticsDTO dto : dSales) { %>
        "<%= dto.getLabel().substring(8) %>일", // "2026-02-01" 형태에서 일자만 추출
        <% } %>
    ];
    const dailyData = [
        <% for(StatisticsDTO dto : dSales) { %> <%= dto.getValue() %>, <% } %>
    ];

    // 차종별 데이터 - 일반, 경차, 장애인 등 차종별 카운트
    const carLabels = [
        <% for(StatisticsDTO dto : cStats) { %> "<%= dto.getLabel() %>", <% } %>
    ];
    const carData = [
        <% for(StatisticsDTO dto : cStats) { %> <%= dto.getValue() %>, <% } %>
    ];

    window.onload = function() {
        renderHourlyChart();
        renderDailyChart();
        renderCarTypeChart();
    };

    // [차트 1] 시간대별 매출(선) + 입차량(막대) 복합 차트
    function renderHourlyChart() {
        const ctx = document.getElementById('hourlyChart').getContext('2d');
        new Chart(ctx, {
            data: {
                labels: hourlyLabels,
                datasets: [
                    {
                        type: 'line',
                        label: '매출액 (원)',
                        data: hourlySalesData,
                        borderColor: '#ef4444',
                        backgroundColor: 'rgba(239, 68, 68, 0.1)',
                        yAxisID: 'y-sales',
                        tension: 0.4,
                        fill: true
                    },
                    {
                        type: 'bar',
                        label: '입차량 (대)',
                        data: hourlyCountData,
                        backgroundColor: '#3b82f6',
                        yAxisID: 'y-counts',
                        borderRadius: 5
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    'y-sales': {
                        type: 'linear',
                        position: 'right',
                        title: { display: true, text: '원' },
                        beginAtZero: true,      // 0부터 시작
                        suggestedMin: 0,        // 데이터가 없어도 최소 0
                        suggestedMax: 100000    // 데이터가 없어도 y축이 최소 100,000원까지는 보이도록 설정
                    },
                    'y-counts': {
                        type: 'linear',
                        position: 'left',
                        title: { display: true, text: '대' },
                        beginAtZero: true,
                        suggestedMin: 0,
                        suggestedMax: 10        // 데이터가 없어도 y축이 최소 10대까지는 보이도록 설정
                        }
                }
            }
        });
    }

    // [차트 2] 일별 매출 차트 (막대)
    function renderDailyChart() {
        const ctx = document.getElementById('dailySalesChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: dailyLabels,
                datasets: [{
                    label: '일 매출액',
                    data: dailyData,
                    backgroundColor: '#10b981',
                    borderRadius: 4
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
    }

    // [차트 3] 차종별 비중 (도넛)
    function renderCarTypeChart() {
        const ctx = document.getElementById('carTypeChart').getContext('2d');
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: carLabels,
                datasets: [{
                    data: carData,
                    backgroundColor: ['#f59e0b', '#6366f1', '#ec4899', '#8b5cf6', '#94a3b8']
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
    }
</script>
</body>
</html>