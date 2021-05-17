package com.utn.MascotApp

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract.Helpers.insert
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.ui.input.key.Key.Companion.Insert
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.room.Insert
import java.util.jar.Manifest



class Filtros : Fragment() {

    lateinit var spinnerTipoMascota: Spinner
    lateinit var spinnerRazaMascota: Spinner
    lateinit var calendario: EditText
    lateinit var camara: Button
    lateinit var editTextEdad: EditText
    lateinit var editTextColor: EditText

    var foto: Uri? = null
    private var REQUEST_GALLERY = 1002
    private var REQUEST_CAMARA = 1002



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        editTextEdad = view.findViewById(R.id.editTextEdad)
        editTextColor = view.findViewById(R.id.editTextColor)




///////////Camera//////////

        camara = view.findViewById(R.id.btnCamara)
        camara.setOnClickListener(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(context?.let { it1 -> ActivityCompat.checkSelfPermission(it1,android.Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED ||
                        context?.let { it1 -> ActivityCompat.checkSelfPermission(it1,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED){

                            val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            requestPermissions(permisosCamara,REQUEST_GALLERY)


                }else {muestraGaleria()}
            }else {muestraGaleria()}

            }




/*

        camara = view.findViewById(R.id.btnCamara)
        val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        abreCamara()
        */
       /* camara.setOnClickListener(){
         fun abreCamara_Click() {
            val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            //if (Request_Camera[0] == PackageManager.PERMISSION_GRANTED)
                abreCamara()
        }}


        camara.setOnClickListener() {
            val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            //if (Request_Camera[0] == PackageManager.PERMISSION_GRANTED)

        }
        */


///////////Spinners///////////
        spinnerTipoMascota = view.findViewById(R.id.spinnerTipoMascota)
        spinnerRazaMascota = view.findViewById(R.id.spinnerRazaMascota)


        spinnerTipoMascota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) {
                    val listaRazaPerro = resources.getStringArray(R.array.RazaMascotaPerro)
                    val adapter = activity?.let {
                        ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, listaRazaPerro)
                    }
                    spinnerRazaMascota.adapter = adapter
                } else if (position == 2) {
                    val listaRazaGato = resources.getStringArray(R.array.RazaMascotaGato)
                    val adapter = activity?.let {
                        ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, listaRazaGato)
                    }
                    spinnerRazaMascota.adapter = adapter
                } else {

                    val listaTamanio = resources.getStringArray(R.array.RazaMascotaVacio)
                    val adapter = activity?.let {
                        ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, listaTamanio)
                    }
                    spinnerRazaMascota.adapter = adapter
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

///////////Calendario///////////

        calendario = view.findViewById(R.id.calendario)

        calendario.setOnClickListener { showDatePickerDialog() }

/*      spinner1 =  view.findViewById(R.id.spinner)
        val listaSexo = resources.getStringArray(R.array.Sexo)
        val adapter = activity?.let {
            ArrayAdapter<String>(it,android.R.layout.simple_spinner_item,listaSexo) }
        spinner1.adapter = adapter
        listaTipo.get(1)

        if (listaTipo.get(1) == 0) {
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY);
        } else {tv.setTextColor(Color.BLACK);
        }
        return view;*/

    }

    ///////////Camera//////////


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_GALLERY -> {if (grantResults[0]==PackageManager.PERMISSION_GRANTED) muestraGaleria()
            else {Toast.makeText(context, "No puedo acceder a la galeria",Toast.LENGTH_LONG).show()} }
        }
    }


    private fun muestraGaleria(){
        val intentGaleria = Intent(Intent.ACTION_PICK)
        intentGaleria.type = "image/*"
        startActivityForResult(intentGaleria,REQUEST_GALLERY)
    }

    private fun abreCamara() {
        val value = ContentValues ()
        value.put(MediaStore.Images.Media.TITLE, "Cargar Imagen")
        foto = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,foto)
        startActivityForResult(camaraIntent,REQUEST_CAMARA)
    }



    ///////////Calendario///////////
    public fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    public fun onDateSelected(day: Int, month: Int, year: Int) {
        calendario.setText("  $day / $month / $year")
    }
    ///////////Calendario///////////

}

