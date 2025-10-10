document.addEventListener('DOMContentLoaded', function() {
    const userMenuBtn = document.getElementById('userMenuBtn');
    const authDropdown = document.getElementById('authDropdown');

    if (userMenuBtn && authDropdown) {
        // Alternar menú al hacer clic en el botón
        userMenuBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            authDropdown.classList.toggle('show');
        });

        // Cerrar menú al hacer clic fuera
        document.addEventListener('click', function(e) {
            if (!userMenuBtn.contains(e.target) && !authDropdown.contains(e.target)) {
                authDropdown.classList.remove('show');
            }
        });

        // Prevenir que el clic en el menú lo cierre
        authDropdown.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    }
});