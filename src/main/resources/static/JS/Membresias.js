// Script para funcionalidad de FAQ
document.addEventListener('DOMContentLoaded', function() {
    const faqQuestions = document.querySelectorAll('.faq-question');

    faqQuestions.forEach(question => {
        question.addEventListener('click', function() {
            const answer = this.nextElementSibling;
            const icon = this.querySelector('i');

            // Cerrar otras respuestas
            document.querySelectorAll('.faq-answer').forEach(item => {
                if (item !== answer && item.classList.contains('active')) {
                    item.classList.remove('active');
                    item.previousElementSibling.querySelector('i').classList.remove('fa-chevron-up');
                    item.previousElementSibling.querySelector('i').classList.add('fa-chevron-down');
                }
            });

            // Alternar respuesta actual
            answer.classList.toggle('active');

            // Cambiar icono
            if (answer.classList.contains('active')) {
                icon.classList.remove('fa-chevron-down');
                icon.classList.add('fa-chevron-up');
            } else {
                icon.classList.remove('fa-chevron-up');
                icon.classList.add('fa-chevron-down');
            }
        });
    });

    // Script para botones de selección de plan
    const planButtons = document.querySelectorAll('.plan-actions .btn');

    planButtons.forEach(button => {
        button.addEventListener('click', function() {
            const planName = this.closest('.plan-card').querySelector('.plan-name').textContent;
            alert(`¡Excelente elección! Has seleccionado el ${planName}. Serás redirigido al proceso de registro.`);
            // Aquí iría la redirección al proceso de registro/pago
        });
    });
});