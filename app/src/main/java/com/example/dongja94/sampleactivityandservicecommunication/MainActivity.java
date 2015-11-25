package com.example.dongja94.sampleactivityandservicecommunication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    IRouteService mRouteService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btn = (Button)findViewById(R.id.btn_route);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRouteService != null) {
                    try {
                        boolean success = mRouteService.startRouting(10, 10, 30);
                        if (!success) {
                            Toast.makeText(MainActivity.this, "another routing running....", Toast.LENGTH_SHORT).show();
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    IRouteCallback.Stub callback = new IRouteCallback.Stub() {
        @Override
        public boolean onRouteChange(int count) throws RemoteException {
            Toast.makeText(MainActivity.this, "count : " + count, Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (mRouteService != null) {
                try {
                    mRouteService.unregisterCallback(callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mRouteService = null;
            }

            if (mRouteService == null) {
                mRouteService = IRouteService.Stub.asInterface(service);
                try {
                    mRouteService.registerCallback(callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRouteService = null;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, RouteService.class);
        startService(intent);
        bindService(intent, mConn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRouteService != null) {
            try {
                mRouteService.unregisterCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mConn);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
