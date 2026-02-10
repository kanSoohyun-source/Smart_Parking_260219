document.addEventListener("click", e => {
    const slot = e.target.closest(".slot-item");
    if (!slot) return;

    const id = slot.dataset.id;
    const isEmpty = slot.dataset.empty === "true";

    location.href = isEmpty
        ? `../entry/entry.jsp?id=${id}`
        : `../exit/exit.jsp?id=${id}`;
});