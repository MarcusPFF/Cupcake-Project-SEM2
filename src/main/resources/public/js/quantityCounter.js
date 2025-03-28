// quantityCounter.js
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.quantity-btn').forEach(button => {
        button.addEventListener('click', function () {
            const container = this.closest('.quantity-controls');
            const quantityElement = container.querySelector('.quantity');
            let quantity = parseInt(quantityElement.textContent);

            if (this.classList.contains('plus')) {
                quantity = Math.min(99, quantity + 1);
            } else {
                quantity = Math.max(0, quantity - 1);
            }

            quantityElement.textContent = quantity;
            // Opdater den skjulte værdi
            const form = container.closest('.quantity-section').querySelector('form');
            form.querySelector('.hidden-quantity').value = quantity;
        });
    });
});

function updateHiddenQuantity(form) {
    const quantity = form.closest('.quantity-section').querySelector('.quantity').textContent;
    form.querySelector('.hidden-quantity').value = quantity;
    return true;
}