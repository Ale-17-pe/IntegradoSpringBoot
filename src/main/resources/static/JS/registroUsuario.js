        document.addEventListener('DOMContentLoaded', function() {
            // Establecer fecha de contrataciÃ³n por defecto como hoy
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('fechaContratacion').value = today;

            // Generar usuario de login automÃ¡ticamente
            const nombreInput = document.getElementById('nombre');
            const apellidoInput = document.getElementById('apellido');
            const usuarioLoginInput = document.getElementById('usuarioLogin');

            function generarUsuarioLogin() {
                if (nombreInput.value && apellidoInput.value) {
                    const nombre = nombreInput.value.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
                    const apellido = apellidoInput.value.toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "");
                    usuarioLoginInput.value = nombre.charAt(0) + apellido.split(' ')[0];
                }
            }

            nombreInput.addEventListener('blur', generarUsuarioLogin);
            apellidoInput.addEventListener('blur', generarUsuarioLogin);

            // ValidaciÃ³n de formulario
            const userForm = document.getElementById('user-form');

            userForm.addEventListener('submit', (e) => {
                e.preventDefault();

                // Validar contraseÃ±as
                const password = document.getElementById('password').value;
                const confirmPassword = document.getElementById('confirmPassword').value;

                if (password !== confirmPassword) {
                    alert('Las contraseñas no coinciden');
                    return;
                }

                // Validar fecha de nacimiento (debe ser anterior a la fecha actual)
                const fechaNacimiento = new Date(document.getElementById('fechaNacimiento').value);
                const hoy = new Date();

                if (fechaNacimiento >= hoy) {
                    alert('La fecha de nacimiento debe ser anterior a la fecha actual');
                    return;
                }

                // Validar DNI (8 dÃ­gitos)
                const dni = document.getElementById('dni').value;
                if (!/^[0-9]{8}$/.test(dni)) {
                    alert('El DNI debe tener exactamente 8 digitos');
                    return;
                }

                // Simular envÃ­o exitoso
                alert('Usuario registrado con Exito');

                // AquÃ­ normalmente enviarÃ­as los datos al servidor
                const formData = {
                    dni: dni,
                    nombre: document.getElementById('nombre').value,
                    apellido: document.getElementById('apellido').value,
                    email: document.getElementById('email').value,
                    telefono: document.getElementById('telefono').value,
                    direccion: document.getElementById('direccion').value,
                    fecha_Nacimiento: document.getElementById('fechaNacimiento').value,
                    fecha_Contratacion: document.getElementById('fechaContratacion').value,
                    usuario_login: document.getElementById('usuarioLogin').value,
                    password: password,
                    rol: document.getElementById('rol').value,
                    estado: document.getElementById('estado').value
                };

                console.log('Datos del usuario:', formData);

                // Limpiar formulario
                userForm.reset();
                document.getElementById('fechaContratacion').value = today;
            });

            // FunciÃ³n para cerrar sesiÃ³n
            const logoutButton = document.getElementById('logoutButton');
            logoutButton.addEventListener('click', function() {
                if(confirm('Estas seguro de que quieres cerrar sesion?')) {
                    setTimeout(function() {
                        window.location.href = 'index.jsp';
                    }, 1000);
                    alert('Sesión cerrada con Exito. Redirigiendo...');
                }
            });
        });