package com.chancorp.tabactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Chan on 2015-10-19.
 */
public class InitialActivity extends AppCompatActivity implements View.OnClickListener{
    Button addFamilyBtn,addUserBtn;
    FamilyData fd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fd=ServerComms.getStaticFamilyData();


        setContentView(R.layout.activity_initial);

        addFamilyBtn=(Button)findViewById(R.id.tut_btn_add_family);
        addUserBtn=(Button)findViewById(R.id.tut_btn_add_user);

        addFamilyBtn.setOnClickListener(this);
        addUserBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final Context ctxt=this;
        if (view.getId()==R.id.tut_btn_add_user){
            UserInformationGetter cg=new UserInformationGetter(ctxt, UserInformationGetter.NAME_AND_PHONE);
            cg.setTitle("Log in to existing family");
            cg.setHint("Family Name","Password");
            cg.setOnReturnListener(new UserInfoReturnListener() {
                public void onReturn(UserInformation c) {
                    final UserInformation cf=c;
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    final ServerComms svc=new ServerComms(ctxt);
                    svc.getID(c.getID());
                    svc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            ServerComms svc2=new ServerComms(ctxt);
                            svc2.addMe(cf.getName(), cf.getPhone());
                            svc2.setDataReturnListener(new DataReturnListener() {
                                @Override
                                public void onReturn(String data) {
                                    close();
                                }
                            });
                        }
                    });


                }
            });
            cg.init();
        }else if (view.getId()==R.id.tut_btn_add_family){
            UserInformationGetter cg=new UserInformationGetter(ctxt, UserInformationGetter.NAME_AND_PHONE);
            cg.setTitle("Make new family");
            cg.setHint("New Family Name","Password");
            cg.setOnReturnListener(new UserInfoReturnListener() {
                public void onReturn(final UserInformation c) {
                    Log.d("Familink", "Cred: " + c.getID() + " | " + c.getPassword());
                    fd.setCredentials(c);
                    final ServerComms svc=new ServerComms(ctxt);
                    svc.addFamily(c);
                    svc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            ServerComms svc2=new ServerComms(ctxt);
                            svc2.addMe(c.getName(), c.getPhone());
                            svc2.setDataReturnListener(new DataReturnListener() {
                                @Override
                                public void onReturn(String data) {
                                    close();
                                }
                            });
                        }
                    });
                }
            });
            cg.init();
        }

    }
    public void close(){
        finish();
    }
}