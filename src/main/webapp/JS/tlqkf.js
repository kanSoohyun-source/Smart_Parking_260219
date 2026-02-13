document.addEventListener("click", e => {
    const slot = e.target.closest(".slot-item");
    if (!slot) return;

    const id = slot.dataset.id;
    const isEmpty = slot.dataset.empty === "true"
    const carNum = slot.dataset.carnum;

    // const contextPath = window.location.pathname.split("/")[1];
    const base = "/" + contextPath;

    location.href = isEmpty
        ? `${contextPath}/input?id=${id}`
        : `${contextPath}/get?id=${id}&carNum=${carNum}`;
});