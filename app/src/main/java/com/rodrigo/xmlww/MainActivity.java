package com.rodrigo.xmlww;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txv_personas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //OBTENIENDO COMPONENTES
        MaterialButton btn_listar = findViewById(R.id.btn_obtener);
        txv_personas = findViewById(R.id.personas);

        btn_listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parserXML();
            }
        });

    }

    public void parserXML(){
        try {
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("data.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            procesandoYParseando(parser);

        } catch (XmlPullParserException | IOException e){
            e.printStackTrace();
        }
    }

    public void procesandoYParseando(XmlPullParser parser){
        ArrayList<Persona> lista_personas = new ArrayList<>();
        try{
            int eventType = parser.getEventType();
            Persona persona = null;

            while (eventType!=XmlPullParser.END_DOCUMENT){
                String eltName=null;

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        eltName=parser.getName();
                        if(eltName.equals("persona")){
                            persona = new Persona();
                            lista_personas.add(persona);
                        } else if (persona!=null){
                            if(eltName.equals("nombres")){
                                persona.setNombres(parser.nextText());
                            }
                            if(eltName.equals("apellidos")){
                                persona.setApellidos(parser.nextText());
                            }
                            if(eltName.equals("edad")){
                                persona.setEdad(Integer.parseInt(parser.nextText()));
                            }
                        }
                        break;
                }
                eventType=parser.next();
            }
            asginarListaATextView(lista_personas);

        } catch (XmlPullParserException | IOException e){
            e.printStackTrace();
        }

    }
    public void asginarListaATextView(ArrayList<Persona> listaPersonas){
        StringBuilder sb = new StringBuilder();
        for (Persona persona : listaPersonas) {
            sb.append(persona.getNombres()+"\n");
            sb.append(persona.getApellidos()+"\n");
            sb.append(persona.getEdad()+"\n");
        }
        txv_personas.setText(sb.toString());
    }
}