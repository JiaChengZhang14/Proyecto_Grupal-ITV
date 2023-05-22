function validarFormulario() {
    var nombre = document.getElementById("nombre").value;
    var apellidos = document.getElementById("apellidos").value;
    var email = document.getElementById("email").value;
    var telefono = document.getElementById("telefono").value;
    var matricula = document.getElementById("matricula").value;

    // Validar nombre y apellidos
    if (nombre.trim() === "" || apellidos.trim() === "") {
      alert("Por favor, ingresa tu nombre y apellidos.");
      return false;
    }

    // Validar email
    if (!validarEmail(email)) {
      alert("Por favor, ingresa un email válido.");
      return false;
    }

    // Validar teléfono
    if (!validarTelefono(telefono)) {
      alert("Por favor, ingresa un número de teléfono válido.");
      return false;
    }

    // Validar matrícula
    if (!validarMatricula(matricula)) {
      alert("Por favor, ingresa una matrícula válida.");
      return false;
    }

    return true;
  }

  function validarEmail(email) {
    // Expresión regular para validar el formato de email
    var regex = /\S+@\S+\.\S+/;
    return regex.test(email);
  }

  function validarTelefono(telefono) {
    // Expresión regular para validar el formato de teléfono (9 dígitos)
    var regex = /^\d{9}$/;
    return regex.test(telefono);
  }

  function validarMatricula(matricula) {
    // Expresión regular para validar el formato de matrícula (4 letras y 3 números)
    var regex = /^\d{4}[A-Z]{3}$/;
    return regex.test(matricula);
  }