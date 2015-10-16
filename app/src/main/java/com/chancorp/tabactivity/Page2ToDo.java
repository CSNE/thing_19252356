package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

//2번째 탭. 할일.
public class Page2ToDo extends Fragment implements RedrawableFragment, AdapterView.OnItemClickListener{

    FamilyData fd;
    ToDoListAdapter tdl;
    ListView lv;


    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        System.out.println("Attached.");
        try{
            this.fd=((FamilyDataProvider) a).provideData();

        }catch(ClassCastException e){
            throw new ClassCastException("Can't cast.");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_2_todo, container, false);


        tdl = new ToDoListAdapter(getContext(), R.layout.single_todo_list_element, fd.getToDosInArray(),(AppCompatActivity)getActivity());


        lv = (ListView) rootView.findViewById(R.id.page2_todolist);
        lv.setAdapter(tdl);
        lv.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void redraw() {
        try {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }catch(IllegalStateException e){
            Log.v("Familink", "IllegalStateException on Page 2>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.v("Familink", "NullPointerException on Page 2>redraw() - Maybe page wasn't active?");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), "CLikcked: "+i, Toast.LENGTH_SHORT).show();
    }
}