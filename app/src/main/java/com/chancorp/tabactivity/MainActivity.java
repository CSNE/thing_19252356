/*
COPYRIGHT CONTROL

Family Logos By Snehal Patil (CC-BY-3.0-US)
Material Design Icons by Google (CC-BY-4.0)
StringUtils class by Nick Frolov


*/

package com.chancorp.tabactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.Random;


//프로그램 시작 시 보여지는 Activity..

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener,FamilyDataProvider,ServerCommsProvider {

    //TODO make bitmaps into mipmaps so no anti-aliasing issues occur.
    //TODO images instead of avatars?
    //TODO Diary page?(instead of notes)
    //TODO text color change in/out


    FamilyData fd;
    ServerComms serverConnector;
    RedrawableFragment[] rdfs;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Familink", "-----------New Session started.-----------");

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        fd=new FamilyData(getApplicationContext());

        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 2. 탭 생성함 create new tabs and and set up the titles of the tabs
        ActionBar.Tab listPageTab = actionbar.newTab().setText("List");
        ActionBar.Tab todoPageTab = actionbar.newTab().setText("To Do");
        //TODO dynamic name - if there's a todo, display that.
        ActionBar.Tab notePageTab = actionbar.newTab().setText("Note");
        ActionBar.Tab chatPageTab = actionbar.newTab().setText("Chat");
        //TODO here too.


        Fragment listPage = (Fragment) new Page1List();
        Fragment todoPage = (Fragment) new Page2ToDo();
        Fragment notePage = (Fragment) new Page3Note();
        Fragment chatPage = (Fragment) new Page4Chat();


        listPageTab.setTabListener(new MyTabsListener(listPage, this));
        todoPageTab.setTabListener(new MyTabsListener(todoPage,this));
        notePageTab.setTabListener(new MyTabsListener(notePage,this));
        chatPageTab.setTabListener(new MyTabsListener(chatPage,this));


        actionbar.addTab(listPageTab);
        actionbar.addTab(todoPageTab);
        actionbar.addTab(notePageTab);
        actionbar.addTab(chatPageTab);


        rdfs=new RedrawableFragment[4];
        rdfs[0]=(RedrawableFragment)listPage;
        rdfs[1]=(RedrawableFragment)todoPage;
        rdfs[2]=(RedrawableFragment)notePage;
        rdfs[3]=(RedrawableFragment)chatPage;




        //ServerComms.setup("http://172.30.86.177:5000",this.fd,rdfs);
        //ServerComms.setup("http://10.0.2.2:8301",this.fd,rdfs);
        ServerComms.setup("http://122.203.53.110:8071",this.fd,rdfs);
        serverConnector = new ServerComms();

        fd.loadFromFile();

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("Familink", "MainActivity started.");

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("Familink", "MainActivity resumed.");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Familink","MainActivity paused.");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("Familink", "MainActivity stopped.");
        fd.saveToFile();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Familink", "MainActivity killed.");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        this.menu=menu;

        return true;

    }

    public Menu getMenu(){
        return this.menu;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //fd.saveToFile();
            startActivity(new Intent(this, Page5Settings.class));
            return true;
        }
        if (id == R.id.global_refresh) {
            Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
            serverConnector.refreshData();
            return true;
        }
        if (id == R.id.debug_del_save) {
            fd.reset();
            serverConnector.redrawFragments();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public FamilyData provideData() {
        return this.fd;
    }
    public ServerComms provideServerComms(){
        return this.serverConnector;
    }


    class MyTabsListener implements ActionBar.TabListener {
    public Fragment fragment;
    public Context context;

    public MyTabsListener(Fragment fragment, Context context) {
        this.fragment = fragment;
        this.context = context;

    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        ft.replace(R.id.tab_area, fragment);
    }



    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        ft.remove(fragment);
    }

}


}
