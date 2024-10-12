package com.example.pruebaapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pruebaapp.R
import com.example.pruebaapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding


    private fun login(){
        val email = binding.txtEmail.text.toString()
        val contrasena = binding.txtPass.text.toString()

        if (formularioValido(email, contrasena)){
            Toast.makeText(this, "Formulario valido", Toast.LENGTH_SHORT).show()
        }

    }

    private fun formularioValido(email: String, contrasena: String): Boolean{
        if(email.isEmpty()) {
            Toast.makeText(this, "Ingrese su correo eléctronico", Toast.LENGTH_SHORT).show()
            return false
        }
        if(contrasena.isEmpty()){
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.btnInicio.setOnClickListener{login()}

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRegistrate.setOnClickListener { goToRegistro() }
    }

    private fun goToRegistro() {
        val i = Intent(this, RegistroActivity::class.java)
        startActivity(i)
    }
}