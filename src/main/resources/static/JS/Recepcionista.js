     document.addEventListener('DOMContentLoaded', function() {
            // Función para cerrar sesión
            const logoutButton = document.getElementById('logoutButton');
            logoutButton.addEventListener('click', function() {
                if(confirm('¿Estás seguro de que quieres cerrar sesión?')) {
                    setTimeout(function() {
                        window.location.href = 'index.jsp';
                    }, 1000);
                    alert('Sesión cerrada con éxito. Redirigiendo...');
                }
            });

            // Simular actualización de estadísticas en tiempo real
            function updateStats() {
                // Simular cambios en las estadísticas
                const randomVisit = Math.floor(Math.random() * 5) + 1;
                const visitorsElement = document.querySelector('.card:nth-child(2) .stat-number');
                const currentVisitors = parseInt(visitorsElement.textContent);
                visitorsElement.textContent = currentVisitors + randomVisit;
                
                // Simular nueva actividad
                const activities = [
                    'Juan Mendoza ingresó al gimnasio',
                    'Nueva reserva para Clase de Spinning',
                    'Pago registrado - Laura Sánchez',
                    'Sofia Castro actualizó su plan a Premium'
                ];
                
                const randomActivity = activities[Math.floor(Math.random() * activities.length)];
                const activityList = document.querySelector('.activity-list');
                
                const newActivity = document.createElement('div');
                newActivity.className = 'activity-item';
                newActivity.innerHTML = `
                    <div class="activity-icon">
                        <i class="fas fa-info-circle"></i>
                    </div>
                    <div class="activity-details">
                        <p>${randomActivity}</p>
                        <span class="activity-time">Justo ahora</span>
                    </div>
                `
                activityList.prepend(newActivity);
                
                // Mantener solo las 4 actividades más recientes
                if (activityList.children.length > 4) {
                    activityList.removeChild(activityList.lastChild);
                }
            }
            
            // Actualizar estadísticas cada 30 segundos
            setInterval(updateStats, 30000);
        });


