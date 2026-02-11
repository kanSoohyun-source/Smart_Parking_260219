document.addEventListener("click", e => {
    const slot = e.target.closest(".slot-item");
    if (!slot) return;

    const id = slot.dataset.id;
    const isEmpty = slot.dataset.empty === "true"
    const carNum = slot.dataset.carnum;

    location.href = isEmpty
        ? `../entry/entry.jsp?id=${id}`
        : `../exit/exit_serch_list.jsp?id=${id}&carNum=${carNum}`;
});