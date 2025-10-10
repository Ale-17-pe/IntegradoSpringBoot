        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('member-form');
            const alertDiv = document.getElementById('form-alert');
            
            // Validar DNI en tiempo real
            const dniInput = document.getElementById('dni');
            dniInput.addEventListener('input', function() {
                this.value = this.value.replace(/[^0-9]/g, '');
                if (this.value.length > 8) {
                    this.value = this.value.slice(0, 8);
                }
            });
            
            // Validar teléfono en tiempo real
            const telefonoInput = document.getElementById('telefono');
            telefonoInput.addEventListener('input', function() {
                this.value = this.value.replace(/[^0-9+()-\s]/g, '');
            });
            
            // Validar nombre y apellido (solo letras y espacios)
            const nombreInput = document.getElementById('nombre');
            const apellidoInput = document.getElementById('apellido');
            
            function validarTexto(input) {
                input.addEventListener('input', function() {
                    this.value = this.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
                });
            }
            
            validarTexto(nombreInput);
            validarTexto(apellidoInput);
            
            // Envío del formulario
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                // Validaciones adicionales
                if (!validarFormulario()) {
                    return;
                }
                
                // Simular envío al servidor
                simularEnvio();
            });
            
            function validarFormulario() {
                const dni = dniInput.value.trim();
                const correo = document.getElementById('correo').value.trim();
                const telefono = telefonoInput.value.trim();
                
                // Validar DNI
                if (dni.length !== 8) {
                    mostrarError('El DNI debe tener exactamente 8 dígitos');
                    return false;
                }
                
                // Validar email
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(correo)) {
                    mostrarError('Por favor, ingrese un correo electrónico válido');
                    return false;
                }
                
                // Validar teléfono (mínimo 9 caracteres)
                if (telefono.replace(/[^0-9]/g, '').length < 9) {
                    mostrarError('El teléfono debe tener al menos 9 dígitos');
                    return false;
                }
                
                return true;
            }
            
            function mostrarError(mensaje) {
                alertDiv.textContent = mensaje;
                alertDiv.className = 'alert alert-error';
                alertDiv.style.display = 'block';
                
                // Ocultar alerta después de 5 segundos
                setTimeout(() => {
                    alertDiv.style.display = 'none';
                }, 5000);
            }
            
            function mostrarExito(mensaje) {
                alertDiv.textContent = mensaje;
                alertDiv.className = 'alert alert-success';
                alertDiv.style.display = 'block';
            }
            
            function simularEnvio() {
                // Mostrar loading
                const submitBtn = form.querySelector('button[type="submit"]');
                const originalText = submitBtn.innerHTML;
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Registrando...';
                submitBtn.disabled = true;
                
                // Simular retraso de red
                setTimeout(() => {
                    // Aquí normalmente se enviarían los datos al servidor
                    const formData = {
                        dni: dniInput.value,
                        nombre: nombreInput.value,
                        apellido: apellidoInput.value,
                        correo: document.getElementById('correo').value,
                        telefono: telefonoInput.value,
                        direccion: document.getElementById('direccion').value,
                        genero: document.getElementById('genero').value
                    };
                    
                    console.log('Datos del miembro:', formData);
                    
                    // Mostrar mensaje de éxito
                    mostrarExito('Miembro registrado exitosamente! Redirigiendo...');
                    
                    // Restaurar botón
                    submitBtn.innerHTML = originalText;
                    submitBtn.disabled = false;
                    
                    // Redirigir después de 2 segundos
                    setTimeout(() => {
                        window.location.href = 'recepcionista.jsp';
                    }, 2000);
                    
                }, 1500);
            }
        });

