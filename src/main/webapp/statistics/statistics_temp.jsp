<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>매출 통계 - 주차장 관리 시스템</title>
    <link rel="stylesheet" href="../CSS/style.css">
    <link rel="stylesheet" href="../CSS/statistics_style.css">
    <%-- chart.js CDN --%>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<%@ include file="../main/menu.jsp" %>

<div class="main-content">
    <div id="statistics" class="page">
        <h2>매출 통계</h2>

        <div class="stats-wrapper">
            <div class="section-card">
                <div class="control-bar">
                    <div class="selector-box">
                        <div class="btn-label">일별 조회</div>
                        <select id="daySelect" onchange="updateDailyChart()"></select>
                    </div>
                    <div class="total-price-tag" id="dayTotal">일 총 매출: 0원</div>
                </div>
                <div class="chart-area">
                    <canvas id="dailyChart"></canvas>
                </div>
            </div>

            <div class="section-card">
                <div class="control-bar">
                    <div class="selector-box">
                        <div class="btn-label">월별 조회</div>
                        <select id="monthSelect" onchange="updateMonthlyChart()"></select>
                    </div>
                    <div class="total-price-tag" id="monthTotal">월 총 매출: 0원</div>
                </div>
                <div class="chart-area">
                    <canvas id="monthlyChart"></canvas>
                </div>
            </div>

            <div class="section-card">
                <div class="control-bar">
                    <div class="selector-box">
                        <div class="btn-label">년도별 조회</div>
                        <select id="daySelect" onchange="updateDailyChart()"></select>
                    </div>
                    <div class="total-price-tag" id="dayTotal">년 총 매출: 0원</div>
                </div>
                <div class="chart-area">
                    <canvas id="yearChart"></canvas>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="../JS/menu.js"></script>
<script>
    let dailyChart, monthlyChart;

    window.onload = function() {
        // 일/월 드롭다운 생성
        const ds = document.getElementById('daySelect');
        const ms = document.getElementById('monthSelect');
        for(let i=1; i<=31; i++) ds.add(new Option(i+'일', i));
        for(let i=1; i<=12; i++) ms.add(new Option(i+'월', i));

        initCharts();
    };

    function initCharts() {
        // 일별 차트 (Line)
        const ctxDay = document.getElementById('dailyChart').getContext('2d');
        dailyChart = new Chart(ctxDay, {
            type: 'line',
            data: {
                labels: ['09시', '12시', '15시', '18시', '21시', '24시'],
                datasets: [{
                    label: '매출액 (원)',
                    data: [120000, 190000, 40000, 60000, 20000, 30000],
                    borderColor: '#2563eb',
                    backgroundColor: 'rgba(37, 99, 235, 0.1)',
                    fill: true,
                    tension: 0.4,
                    borderWidth: 3
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });

        /* 수정 필요 */
        document.getElementById('dayTotal').innerText = "일 총 매출: 460,000원";

        // 월별 차트 (Bar)
        const ctxMonth = document.getElementById('monthlyChart').getContext('2d');
        monthlyChart = new Chart(ctxMonth, {
            type: 'bar',
            data: {
                labels: ['1주차', '2주차', '3주차', '4주차'],
                datasets: [{
                    label: '매출액 (원)',
                    data: [450000, 600000, 320000, 800000],
                    backgroundColor: '#10b981',
                    borderRadius: 8
                }]
            },
            options: { responsive: true, maintainAspectRatio: false }
        });
        document.getElementById('monthTotal').innerText = "월 총 매출: 2,170,000원";
    }

    // 데이터 업데이트 가짜 함수 (실제 연결 시 AJAX 사용)
    function updateDailyChart() {
        const newData = Array.from({length: 6}, () => Math.floor(Math.random() * 200000));
        dailyChart.data.datasets[0].data = newData;
        dailyChart.update();
        const total = newData.reduce((a, b) => a + b, 0);
        document.getElementById('dayTotal').innerText = `일 총 매출: ${total.toLocaleString()}원`;
    }

    function updateMonthlyChart() {
        const newData = Array.from({length: 4}, () => Math.floor(Math.random() * 1000000));
        monthlyChart.data.datasets[0].data = newData;
        monthlyChart.update();
        const total = newData.reduce((a, b) => a + b, 0);
        document.getElementById('monthTotal').innerText = `월 총 매출: ${total.toLocaleString()}원`;
    }
</script>
</body>
</html>