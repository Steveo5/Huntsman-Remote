package steven.hotmail.com.webcrawlerclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Get the console fragment
    private ConsoleFragment consoleFragment = new ConsoleFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textConnectedTitle = (TextView) findViewById(R.id.txtConnectedTitle);
        TextView textConnectedMinor = (TextView) findViewById(R.id.txtTitleMinor);


        //textConnectedTitle.setText("Connected to " + ClientListener.instance().getAddress());

        // Get the main fragment
        final frgMain mainFrag = new frgMain();

        // Show the main fragment by default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, mainFrag, "main_frag")
                .addToBackStack(null)
                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the console fragment to be in view
                if(isConsoleShowing())
                {
                    hideConsole();
                } else
                {
                    showConsole();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Show the console fragment
     */
    public void showConsole()
    {

        consoleFragment = new ConsoleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, consoleFragment, "fragment_console")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Hide the console fragment
     */
    public void hideConsole()
    {
        consoleFragment.getActivity().onBackPressed();
    }

    /**
     * Check if the console fragment is showing
     */
    public boolean isConsoleShowing()
    {
        return consoleFragment.isVisible();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            // Send to dashboard
            case R.id.mnuDashboard:
                // Show the main fragment by default
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_layout, new frgMain(), "main_frag")
                        .addToBackStack(null)
                        .commit();
                break;
            // Send to indexer
            case R.id.mnuIndexer:
                // Show the main fragment by default
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_layout, new IndexerFragment(), "indexer_frag")
                        .addToBackStack(null)
                        .commit();
                break;
            // Send to search
            case R.id.mnuSearch:
                // Show the main fragment by default
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_layout, new SearchFragment(), "search_frag")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.mnuClose:
                ClientListener.instance().closeConnection();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ClientListener.instance().closeConnection();
    }
}
