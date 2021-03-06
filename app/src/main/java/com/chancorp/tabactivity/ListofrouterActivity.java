package com.chancorp.tabactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListofrouterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn, refBtn;
    ListView listview;
    CustomAdapter01 adapter = null;
    final int REQUEST_CODE = 11131, MaxRouterSize = 10;
    FamilyData fd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofrouter);

        //fd=new FamilyData(this);
        //fd.loadFromFile();
        fd=ServerComms.getStaticFamilyData();


        listview = (ListView) findViewById(R.id.listofrouterlist);
        adapter = new CustomAdapter01(this, R.layout.customadapter01design, fd);
        listview.setAdapter(adapter);
        btn = (Button) findViewById(R.id.addrouterbtn);
        refBtn=(Button) findViewById(R.id.router_refresh);
        btn.setOnClickListener(this);
        refBtn.setOnClickListener(this);

        Log.d("Familink", "Activity_list_of_router starting. Router data: " + fd.getRouterListAsString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listofrouter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.listofrouterbackarrow) {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addrouterbtn) {
            startActivityForResult(new Intent(this, AddrouterActivity.class), REQUEST_CODE);
        }else if (v.getId()==R.id.router_refresh){
            final ServerComms sc=new ServerComms(getApplicationContext());
            sc.setDataReturnListener(new DataReturnListener() {
                @Override
                public void onReturn(String data) {
                    adapter.notifyDataSetChanged();
                    sc.clearDataReturnListener();
                }
            });
            sc.refreshData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                RouterInformation newRouter=new RouterInformation();
                newRouter.setMacAddr(data.getStringExtra("new_mac"));
                newRouter.setName(data.getStringExtra("new_name"));

                if(!fd.matchRouter(newRouter)) {
                    Log.d("Familink", "New router: " + newRouter.toString());

                    final ServerComms sc=new ServerComms(getApplicationContext());
                    sc.setDataReturnListener(new DataReturnListener() {
                        @Override
                        public void onReturn(String data) {
                            adapter.notifyDataSetChanged();
                            listview.invalidate();
                            sc.clearDataReturnListener();
                        }
                    });
                    sc.addRouter(newRouter);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d("Familink", "Router duplicate.");
                    Toast.makeText(this, "이 공유기는 이미 목록에 있는 공유기입니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
        return;
    }

    public void datastechanged(int position) {
        ServerComms sc=new ServerComms(getApplicationContext());
        sc.setDataReturnListener(new DataReturnListener() {
            @Override
            public void onReturn(String data) {
                adapter.notifyDataSetChanged();
                listview.invalidate();
            }
        });
        sc.deleteRouter(fd.getRouters().get(position));
        return;
    }



    class CustomAdapter01 extends BaseAdapter implements View.OnClickListener {

        int mResource;
        FamilyData fd;
        LayoutInflater minflater;

        public CustomAdapter01(Context context,int layoutId, FamilyData fd) {
            mResource = layoutId;
            this.fd=fd;
            minflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            try {
                return fd.getRouters().size();
            } catch(NullPointerException e) {return 0;}
        }

        @Override
        public Object getItem(int position) {
            return fd.getRouters().get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        private class CustomHolder {
            TextView mtextview;
            ImageView imgview01, imgview02;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {
            final int position_ = position;
            final Context context = parent.getContext();
            CustomHolder holder = null;
            TextView text1 = null;
            ImageView img1 = null, img2 = null;

            // 아이템 뷰 생성 및 재사용
            if(convertview == null) {
                convertview = minflater.inflate(mResource, parent, false);
                holder = new CustomHolder();
                holder.mtextview = text1;
                holder.imgview01 = img1;
                holder.imgview02 = img2;
                convertview.setTag(holder);
            } else {
                holder = (CustomHolder) convertview.getTag();
                holder.mtextview = text1;
                holder.imgview01 = img1;
                holder.imgview02 = img2;
            }

            //UI 레퍼런스 가져오기
            text1 = (TextView) convertview.findViewById(R.id.customadapter01text01);
            img1 = (ImageView) convertview.findViewById(R.id.customadapter01img01);
            img2 = (ImageView) convertview.findViewById(R.id.customadapter01img02);
            text1.setText(fd.getRouters().get(position).summary());
            img2.setImageResource(R.drawable.icon_delete);

            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Caution");
                    Log.d("ListofrouterActivity", String.valueOf(position_));
                            builder.setMessage(fd.getRouters().get(position_).summary() + "을 목록에서 지울까요?")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    datastechanged(position_);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog_ = builder.create();
                    dialog_.show();
                }
            });
            return convertview;
        }
        @Override
        public void onClick(View v) {}
    }

}
