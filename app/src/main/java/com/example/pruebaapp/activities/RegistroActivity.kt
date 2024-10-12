package com.example.pruebaapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruebaapp.R
import com.example.pruebaapp.databinding.ActivityRegistroBinding
import com.example.pruebaapp.models.Cliente
import com.example.pruebaapp.providers.AuthProvider
import com.example.pruebaapp.providers.ClienteProvider


class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val authProvider = AuthProvider()
    private val clienteProvider = ClienteProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnInicio.setOnClickListener { goToSesion() }
        binding.btnRegistrate.setOnClickListener { registro() }
    }

    private fun registro() {
        val nombre = binding.textFieldName.text.toString()
        val apellido = binding.textFieldLastname.text.toString()
        val rut = binding.textRut.text.toString()
        val telefono = binding.textFieldPhone.text.toString()
        val correo = binding.textFieldEmail.text.toString()
        val contrasena = binding.textFieldPassword.text.toString()
        val confirContrasena = binding.textFieldConfirmPassword.text.toString()
        if (formularioValido(
                nombre,
                apellido,
                rut,
                telefono,
                correo,
                contrasena,
                confirContrasena
            )
        ) {
            authProvider.registro(correo, contrasena).addOnCompleteListener {

                if (it.isSuccessful) {
                    val cliente = Cliente(
                        id = authProvider.getId(),
                        nombre = nombre,
                        apellido = apellido,
                        rut = rut,
                        telefono = telefono,
                        correo = correo,
                        contrasena = contrasena
                    )
                    clienteProvider.create(cliente).addOnCompleteListener {
                        Toast.makeText(
                            this@RegistroActivity,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
                        goToSesion()
                    }
                } else {
                    it
                    Toast.makeText(
                        this@RegistroActivity,
                        "Registro fallido ${it.exception.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun formularioValido(
        nombre: String,
        apellido: String,
        rut: String,
        telefono: String,
        correo: String,
        contrasena: String,
        confirContrasena: String

    ): Boolean {
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show()
            return false
        }
        if (apellido.isEmpty()) {
            Toast.makeText(this, "Ingrese su apellido", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validarRut(rut)) {
            Toast.makeText(this, "Ingrese su Rut", Toast.LENGTH_SHORT).show()
            return false
        }
        if (telefono.isEmpty()) {
            Toast.makeText(this, "Ingrese su teléfono", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validarCorreo(correo)) {
            Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
            return false
        }
        if (contrasena.isEmpty()) {
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (confirContrasena.isEmpty()) {
            Toast.makeText(this, "Confirmar contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (contrasena != confirContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            return false
        }
        if (contrasena.length < 6) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 6 caracteres",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun goToSesion() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun validarCorreo(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validarRut(rut: String): Boolean {
        val rutClean = rut.replace(".", "").replace("-", "") // Elimina puntos y guión
        if (rutClean.length < 8 || rutClean.length > 9) return false // Longitud inválida

        val rutBody = rutClean.dropLast(1) // Extraer el cuerpo del RUT
        val dv = rutClean.takeLast(1) // Extraer el dígito verificador

        // Validar que el cuerpo del RUT sea numérico
        if (!rutBody.all { it.isDigit() }) return false

        val dvCalculated = calculateDV(rutBody.toInt()) // Calcular el dígito verificador esperado

        // Comprobar si el dígito verificador calculado coincide con el ingresado
        return dv.equals(dvCalculated, ignoreCase = true)
    }

    fun calculateDV(rut: Int): String {
        var num = rut
        var multiplier = 2
        var sum = 0

        while (num > 0) {
            sum += (num % 10) * multiplier
            num /= 10
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }

        val remainder = 11 - (sum % 11)

        return when (remainder) {
            11 -> "0"
            10 -> "K"
            else -> remainder.toString()
        }
    }

}


