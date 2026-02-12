
const form = document.getElementById("searchForm");
const keywordInput = document.querySelector("input[name='keyword']");
const slots = document.querySelectorAll(".slot-item");
const resetBtn = document.getElementById("resetBtn");

form.addEventListener("submit", function (e) {
    e.preventDefault();

    const keyword = keywordInput.value.trim();
    if (keyword === "") {
        slot.forEach(slot => {
            if (slot.classList.contains("occupied")) {
                slot.style.display = "block";
            } else {
                slot.style.display = "none";
            }
        });
        return;
    }
    slots.forEach(slot => {


        if (!slot.classList.contains("occupied")) {
            slot.style.display = "none";
            return;
        }

        const carNum = slot.dataset.carnum;

        if (!carNum) {
            slot.style.display = "none";
            return;
        }
        if (carNum.includes(keyword)) {
            slot.style.display = "block";
        } else {
            slot.style.display = "none";
        }

        resetBtn.addEventListener("click", function () {
            keywordInput.value = "";
            slots.forEach(slot => {
                slot.style.display = "block";
            });
        });
    });
});