        document.addEventListener('DOMContentLoaded', function() {
            const navItems = document.querySelectorAll('.nav-item');
            navItems.forEach(item => {
                item.addEventListener('click', function() {
                    navItems.forEach(i => i.classList.remove('active'));
                    this.classList.add('active');
                });
            });
            const logoutButton = document.getElementById('logoutButton');
            logoutButton.addEventListener('click', function() {
                if(confirm('Estas seguro de que quieres cerrar sesion')) {
                    setTimeout(function() {
                        window.location.href = 'index.jsp';
                    }, 1000);
                    alert(' Redirigiendo...');
                }
            });

            const cards = document.querySelectorAll('.card');
            cards.forEach(card => {
                card.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-5px)';
                });

                card.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0)';
                });
            });
        });

