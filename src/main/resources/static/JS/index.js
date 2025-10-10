    document.addEventListener('DOMContentLoaded', function() {
        // Contador de estadísticas
        const counters = document.querySelectorAll('.stat-item h3');
        const speed = 200;

        counters.forEach(counter => {
            const target = parseInt(counter.getAttribute('data-count'));
            let count = 0;
            const increment = Math.ceil(target / speed);

            const updateCount = () => {
                if (count < target) {
                    count += increment;
                    if (count > target) count = target;
                    counter.innerText = count;
                    setTimeout(updateCount, 1);
                }
            };

            // Activar cuando el elemento es visible
            const observer = new IntersectionObserver(entries => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        updateCount();
                        observer.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.5 });

            observer.observe(counter);
        });

        // Menú móvil
        const menuBtn = document.querySelector('.mobile-menu-btn');
        const nav = document.querySelector('.main-nav');
        const headerActions = document.querySelector('.header-actions');

        if (menuBtn) {
            menuBtn.addEventListener('click', () => {
                nav.classList.toggle('active');
                if (headerActions) {
                    headerActions.classList.toggle('active');
                }
                menuBtn.querySelector('i').classList.toggle('fa-bars');
                menuBtn.querySelector('i').classList.toggle('fa-times');
            });
        }

        // Cerrar menú al hacer clic en un enlace
        const navLinks = document.querySelectorAll('.main-nav a');
        navLinks.forEach(link => {
            link.addEventListener('click', () => {
                nav.classList.remove('active');
                if (headerActions) {
                    headerActions.classList.remove('active');
                }
                if (menuBtn) {
                    menuBtn.querySelector('i').classList.add('fa-bars');
                    menuBtn.querySelector('i').classList.remove('fa-times');
                }
            });
        });

        // Cerrar menú al hacer clic fuera de él
        document.addEventListener('click', (e) => {
            if (nav.classList.contains('active') &&
                !e.target.closest('.main-nav') &&
                !e.target.closest('.mobile-menu-btn') &&
                !e.target.closest('.header-actions')) {
                nav.classList.remove('active');
                if (headerActions) {
                    headerActions.classList.remove('active');
                }
                if (menuBtn) {
                    menuBtn.querySelector('i').classList.add('fa-bars');
                    menuBtn.querySelector('i').classList.remove('fa-times');
                }
            }
        });

        // Carga diferida del video de YouTube cuando es visible
        const videoSection = document.querySelector('.video-section');
        if (videoSection) {
            const videoObserver = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const iframe = document.querySelector('.video-container iframe');
                        if (iframe && !iframe.src.includes('autoplay=1')) {
                            iframe.src += '&autoplay=1&mute=1';
                        }
                        videoObserver.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.5 });

            videoObserver.observe(videoSection);
        }

        // Efecto de zoom en imágenes al hacer hover
        const zoomImages = document.querySelectorAll('.about-image, .experience-image, .goal-image');
        zoomImages.forEach(image => {
            image.addEventListener('mouseenter', () => {
                const img = image.querySelector('img');
                if (img) {
                    img.style.transform = 'scale(1.05)';
                }
            });

            image.addEventListener('mouseleave', () => {
                const img = image.querySelector('img');
                if (img) {
                    img.style.transform = 'scale(1)';
                }
            });
        });

        // Smooth scroll para enlaces internos
        const internalLinks = document.querySelectorAll('a[href^="#"]');
        internalLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                const targetId = this.getAttribute('href');
                if (targetId === '#') return;

                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    targetElement.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });

        // Prevenir el envío duplicado de formularios
        const forms = document.querySelectorAll('form');
        forms.forEach(form => {
            form.addEventListener('submit', function() {
                const submitBtn = this.querySelector('button[type="submit"], input[type="submit"]');
                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.value = 'Procesando...';
                }
            });
        });
        document.addEventListener('DOMContentLoaded', function() {
            // Menú móvil
            const menuBtn = document.querySelector('.mobile-menu-btn');
            const nav = document.querySelector('.main-nav');
            const headerActions = document.querySelector('.header-actions');

            if (menuBtn) {
                menuBtn.addEventListener('click', () => {
                    nav.classList.toggle('active');
                    if (headerActions) {
                        headerActions.classList.toggle('active');
                    }
                    menuBtn.querySelector('i').classList.toggle('fa-bars');
                    menuBtn.querySelector('i').classList.toggle('fa-times');
                });
            }

            // Cerrar menú al hacer clic en un enlace
            const navLinks = document.querySelectorAll('.main-nav a');
            navLinks.forEach(link => {
                link.addEventListener('click', () => {
                    nav.classList.remove('active');
                    if (headerActions) {
                        headerActions.classList.remove('active');
                    }
                    if (menuBtn) {
                        menuBtn.querySelector('i').classList.add('fa-bars');
                        menuBtn.querySelector('i').classList.remove('fa-times');
                    }
                });
            });

            // Cerrar menú al hacer clic fuera de él
            document.addEventListener('click', (e) => {
                if (nav.classList.contains('active') &&
                    !e.target.closest('.main-nav') &&
                    !e.target.closest('.mobile-menu-btn') &&
                    !e.target.closest('.header-actions')) {
                    nav.classList.remove('active');
                    if (headerActions) {
                        headerActions.classList.remove('active');
                    }
                    if (menuBtn) {
                        menuBtn.querySelector('i').classList.add('fa-bars');
                        menuBtn.querySelector('i').classList.remove('fa-times');
                    }
                }
            });
        });
    });
    
