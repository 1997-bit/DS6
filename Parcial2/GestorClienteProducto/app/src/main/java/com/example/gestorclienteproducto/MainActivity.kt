package com.example.gestorclienteproducto

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gestorclienteproducto.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnClientes = findViewById<LinearLayout>(R.id.btnClientes)
        val btnProductos = findViewById<LinearLayout>(R.id.btnProductos)
        val btnOrdenes =findViewById<LinearLayout>(R.id.btnOrdenes)
        val btnSalir = findViewById<LinearLayout>(R.id.btnSalir)

        //pantalla  cliente
        btnClientes.setOnClickListener{
            val intent = Intent(this, Activity_Clientes::class.java)
            startActivity(intent)
        }
        //fin pantalla cliente

        //pantalla Productos
        btnProductos.setOnClickListener{
            startActivity(Intent(this, Activity_Productos::class.java))

        }
        //fin pantalla Productos

        //pantalla Ordenes
        btnOrdenes.setOnClickListener{
            startActivity(Intent(this, Activity_ordenes::class.java))

            //por hacer
        }
        //fin pantalla Ordenes

        //pantalla salir
        btnSalir.setOnClickListener{
            finish()
        }
        //fin pantalla salir





        }
    }
