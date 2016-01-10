package testservices.vvkservices.rhcloud.com.testservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    private Button startBtn;
    private Button bindBtn;
    private TextView textView1;

    public OnFragmentElemClickListener mListener;

    public interface OnFragmentElemClickListener {
        void onElementClick(View view);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainActivityFragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        startBtn = (Button) mainActivityFragmentView.findViewById(R.id.BtnStartService);
        bindBtn = (Button) mainActivityFragmentView.findViewById(R.id.BtnBindService);
        textView1 = (TextView) mainActivityFragmentView.findViewById(R.id.text1);
        return mainActivityFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        startBtn.setOnClickListener(this);
        bindBtn.setOnClickListener(this);
        textView1.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentElemClickListener) {
            mListener = (OnFragmentElemClickListener) context;
        }
    }

    @Override
    public void onClick(View v) {
        mListener.onElementClick(v);
    }

    /**
     * метод для обновления текста TextView из основной активности
     */
    public void setTextView1(String newText){
        textView1.setText(newText);
    }
}
