package com.minioguille.pruebamapas;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.minioguille.pruebamapas.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final float CAMARA_ZOOM = 11.7f;
    private static final LatLng COORDENADAS_MAR_DEL_PLATA = new LatLng(-38.00042, -57.5562);
    private static final String JSON_CAMPO_RESULTADO = "centros";
    private static final String PATH_API_URL = "TU_URL";

    private ActivityMapsBinding binding;
    private CentroSalud[] mCentrosSalud;

    private GoogleMap mGoogleMap;

    private TextView txNombre;
    private TextView txServicios;
    private TextView txHorarios;
    private TextView txDireccion;
    private TextView txContacto;

    private TextView txCargando;
    private ProgressBar pbCargando;

    private boolean algoParaMostrar;

    private SupportMapFragment mapFragment;

    private ScrollView clContenedorDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Obtengo el componente visual que mostrará el mapa
		//y lo mantengo oculto hasta que el mapa esté listo (cargado)
		mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getView().setVisibility(View.GONE);

        enlazarUIComponentes();

        clContenedorDatos.setVisibility(View.GONE);

        algoParaMostrar = false;

		//Hago la llamada a la api para obtener los datos
        obtenerCentros();

    }

    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        configurarMapa();

        mapFragment.getView().setVisibility(View.VISIBLE);
        txCargando.setVisibility(View.GONE);
        pbCargando.setVisibility(View.GONE);

        aniadirEvento();
    }

    private void enlazarUIComponentes(){
        txNombre          = findViewById(R.id.txNombre);
        txServicios       = findViewById(R.id.txDescripcion);
        txHorarios        = findViewById(R.id.txHorarios);
        txDireccion       = findViewById(R.id.txDireccion);
        txContacto        = findViewById(R.id.txContacto);
        clContenedorDatos = findViewById(R.id.contenedorDatosCentros);

        txCargando = findViewById(R.id.txCargandoMapa);
        pbCargando = findViewById(R.id.pgCargando);
    }

    private void obtenerCentros(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PATH_API_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if ( !response.isNull(JSON_CAMPO_RESULTADO)){

                    JSONArray centrosSalud = null;
                    try {
						
						//Obtuve una respuesta correcta
						//Ahora obtengo los modelos de centros
						//y aviso que el mapa está listo para mostrarse
						
                        centrosSalud = response.getJSONArray(JSON_CAMPO_RESULTADO);

                        int centrosTotales = centrosSalud.length();
                        mCentrosSalud = new CentroSalud[centrosTotales];

                        for ( int i = 0; i < centrosTotales; i++){
                            Gson gson = new Gson();
                            mCentrosSalud[i] = gson.fromJson(String.valueOf(centrosSalud.getJSONObject(i)),CentroSalud.class);
                        }

                        mapFragment.getMapAsync(MapsActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("APLICACION",error.toString());
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void configurarMapa(){
		//Con los datos obtenidos desde la Api, configuro el mapa
		//la idea es que el usuario solo pueda tocar y desplazar el mapa
		//pero no hacer zoom (para evitar perder los puntos marcados)
        this.mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        this.mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        for (CentroSalud centroSalud : mCentrosSalud) {
            LatLng posicion = new LatLng(centroSalud.getMapa_latitud_centro(), centroSalud.getMapa_longitud_centro());
            this.mGoogleMap.addMarker(new MarkerOptions().position(posicion).title(centroSalud.getNombre_centro())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icono_places))
                    .infoWindowAnchor(0.5f, 0.5f));

        }

        this.mGoogleMap.setContentDescription("CENTROS DE SALUD MAR DEL PLATA");

        CameraPosition camPos = new CameraPosition.Builder().target(COORDENADAS_MAR_DEL_PLATA).zoom(CAMARA_ZOOM).build();
        CameraUpdate cam = CameraUpdateFactory.newCameraPosition(camPos);
        this.mGoogleMap.animateCamera(cam);
        this.mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    private void aniadirEvento() {
		//Para mostrar los datos cuando se toca un centro
        this.mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                algoParaMostrar = true;

                String id = marker.getId().substring(marker.getId().length()-1);
                int idCentroTouched = Integer.parseInt(id);

                txNombre.setText(mCentrosSalud[idCentroTouched].getNombre_centro());
                txServicios.setText(mCentrosSalud[idCentroTouched].getDescripcion_centro());
                txHorarios.setText(mCentrosSalud[idCentroTouched].getHorario_atencion_centro());
                txDireccion.setText(mCentrosSalud[idCentroTouched].getDireccion_centro());
                txContacto.setText(mCentrosSalud[idCentroTouched].getContacto_centro());

                if ( algoParaMostrar)
                    clContenedorDatos.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }
}