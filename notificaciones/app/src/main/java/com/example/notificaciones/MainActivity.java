package com.example.notificaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NativeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText veditnombre;
    private EditText veditcorreo;
    private EditText veditcontra;
    private Button vbotonregistrar;
    //variable de los datos nesesarios para registrar
    private String nombre="";
    private String correo="";
    private String contra="";

    FirebaseAuth vAuth;
    DatabaseReference vBaseDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vAuth=FirebaseAuth.getInstance();
        vBaseDatos = FirebaseDatabase.getInstance().getReference();

        veditnombre = (EditText) findViewById(R.id.editnombre);
        veditcorreo = (EditText) findViewById(R.id.editemail);
        veditcontra = (EditText) findViewById(R.id.editcontra);
        vbotonregistrar = (Button) findViewById(R.id.botonregistrar);

        vbotonregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = veditnombre.getText().toString();
                correo = veditcorreo.getText().toString();
                contra = veditcontra.getText().toString();

                if (!nombre.isEmpty() && !correo.isEmpty() && !contra.isEmpty()){
                    if (contra.length()>=6){

                    }else {
                        Toast.makeText(MainActivity.this,"la contraseña debe tener almenos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                    registrarUsuario();
                }else   {
                    Toast.makeText(MainActivity.this,"debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }

            private void registrarUsuario(){
                vAuth.createUserWithEmailAndPassword(correo, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Map<String, Object> map = new HashMap<>();
                            map.put("nombre", nombre);
                            map.put("correo", correo);
                            map.put("contra", contra);
                            String id = vAuth.getCurrentUser().getUid();
                            vBaseDatos.child("usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()){
                                        startActivity(new Intent(MainActivity.this, PerfilMainActivity.class));
                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(MainActivity.this, "no se pudo registrar el usuario ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });



    }
}