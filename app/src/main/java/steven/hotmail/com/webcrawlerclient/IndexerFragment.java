package steven.hotmail.com.webcrawlerclient;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndexerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndexerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndexerFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Holds the status label
    private TextView lblStatus = null, lblTime = null, lblQueued = null;

    // Holds the time when the crawler began
    long beginTime = System.currentTimeMillis();
    int queueSize = 0;

    private OnFragmentInteractionListener mListener;

    public IndexerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndexerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndexerFragment newInstance(String param1, String param2) {
        IndexerFragment fragment = new IndexerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_indexer, container, false);

        // Hold the text status
        final EditText txtWebsite = (EditText) rootView.findViewById(R.id.txtSiteToIndex);
        lblStatus = (TextView) rootView.findViewById(R.id.lblStatus);
        lblTime = (TextView) rootView.findViewById(R.id.txtTime);
        lblQueued = (TextView) rootView.findViewById(R.id.txtQueued);
        final EditText txtWebsiteToQueue = (EditText) rootView.findViewById(R.id.txtSiteToIndex);
        Button btnQueue = (Button) rootView.findViewById(R.id.btnQueue);
        Button btnStart = (Button) rootView.findViewById(R.id.btnStart);
        Button btnStop = (Button) rootView.findViewById(R.id.btnStop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientListener.instance().startIndexing();
            }
        });

        btnQueue.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                // Do some basic input validation client side
                if(txtWebsiteToQueue.getText().length() > 3) {
                    // Send a notification
                    Toast toast = Toast.makeText(getContext(), "Website queued", Toast.LENGTH_SHORT);
                    toast.show();
                    // Queue the website to be crawled
                    ClientListener.instance().queueWebsite(txtWebsiteToQueue.getText().toString());
                    txtWebsiteToQueue.setText("");
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientListener.instance().stopIndexing();
            }
        });

        /**
         * Start the ui updater thread
         */
        new Thread()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    update();

                    // Sleep/interrupt the thread as to not hog cpu time
                    try {
                        Thread.sleep(300L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread()
        {
            @Override
            public void run()
            {
                while(true) {
                    clock();
                    // Sleep the thread 1 second
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return rootView;
    }

    // Executed once per second
    private void clock()
    {
        // The time in seconds since the crawler started
        final float timeInSeconds = (System.currentTimeMillis() - beginTime) / 1000L;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lblTime.setText(TimeUtil.secondsToTime(timeInSeconds));
            }
        });
    }

    /**
     * Updates the Indexer fragment ui
     */
    private void update()
    {
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                if(ClientListener.instance().isRunning())
                {
                    lblStatus.setBackgroundColor(Color.parseColor("#7AE80C"));
                } else
                {
                    lblStatus.setBackgroundColor(Color.parseColor("#E80C0C"));
                }
                // Update the queue size
                lblQueued.setText(ClientListener.instance().getQueueSize() + "");
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
