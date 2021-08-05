package br.ufma.lsdi.cddlbaseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import br.ufma.lsdi.cddl.CDDL;
import br.ufma.lsdi.cddl.ConnectionFactory;
import br.ufma.lsdi.cddl.listeners.IConnectionListener;
import br.ufma.lsdi.cddl.network.ConnectionImpl;

public class MainActivity extends AppCompatActivity {

    private String host;
    private CDDL cddl;
    private ConnectionImpl connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPermission();
        initCDDL();
    }

    private IConnectionListener connectionListener = new IConnectionListener() {

        @Override
        public void onConnectionEstablished() {
            Log.d(null, "Conexão estabelecida.");
        }

        @Override
        public void onConnectionEstablishmentFailed() {
            Log.d(null, "Falha na conexão.");
        }

        @Override
        public void onConnectionLost() {
            Log.d(null, "Conexão perdida.");
        }

        @Override
        public void onDisconnectedNormally() {
            Log.d(null, "Uma disconexão normal ocorreu.");
        }
    };

    private void initCDDL() {
        //host = "192.168.18.12";
        host = CDDL.startMicroBroker();
        connection = ConnectionFactory.createConnection();
        connection.setClientId("luis");
        connection.setHost(host);
        connection.addConnectionListener(connectionListener);
        connection.connect();
        cddl = CDDL.getInstance();
        cddl.setConnection(connection);
        cddl.setContext(this);
        cddl.startService();
        cddl.startCommunicationTechnology(CDDL.INTERNAL_TECHNOLOGY_ID);
    }

    private void setPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}