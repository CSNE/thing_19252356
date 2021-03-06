package com.chancorp.tabactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//3번째 탭. 가족 노트.
public class Page3Note extends Fragment implements Redrawable, AdapterView.OnItemClickListener, View.OnClickListener{

    FamilyData fd;
    NoteListAdapter nla;
    ListView lv;
    Button btn;



    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        System.out.println("Attached.");
        this.fd=ServerComms.getStaticFamilyData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_3_notes, container, false);

        nla = new NoteListAdapter(R.layout.single_note_list_element, fd,(AppCompatActivity)getActivity());


        lv = (ListView) rootView.findViewById(R.id.page3_notelist);
        lv.setAdapter(nla);
        lv.setOnItemClickListener(this);

        btn=(Button) rootView.findViewById(R.id.page3_add_button);
        btn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void redraw() {
        try {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }catch(IllegalStateException e){
            Log.v("Familink", "IllegalStateException on Page 3>redraw() - Maybe page wasn't visible?");
        }catch(NullPointerException e){
            Log.v("Familink", "NullPointerException on Page 3>redraw() - Maybe page wasn't active?");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getContext(), "CLikcked: " + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.page3_add_button){
            Log.d("Familink", "Page 3 add button clicked");
            UserInformationGetter uig=new UserInformationGetter(getContext(),UserInformationGetter.NOTE);
            uig.setTitle("새 노트");
            uig.setOnReturnListener(new UserInfoReturnListener() {
                @Override
                public void onReturn(UserInformation c) {
                    Note nt=new Note();
                    nt.setTitle(c.getNoteTitle());
                    nt.setBody(c.getNoteBody());
                    new ServerComms(getContext()).addNote(nt);
                    redraw();
                }
            });
            uig.init();

        }else{
            Log.wtf("Familink", "Page 2 - What the hell did you press?!?!?");
        }
    }
}