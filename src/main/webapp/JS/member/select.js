function selectMember(carNum) {
    location.href = '/member/member_detail?carNum=' + encodeURIComponent(carNum);
}