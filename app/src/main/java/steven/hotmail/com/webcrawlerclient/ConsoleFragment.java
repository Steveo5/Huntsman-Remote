package steven.hotmail.com.webcrawlerclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsoleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsoleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConsoleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsoleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsoleFragment newInstance(String param1, String param2) {
        ConsoleFragment fragment = new ConsoleFragment();
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

        new Thread()
        {
            @Override
            public void run()
            {
                int lastMessageRecieved = 1;



                // Continue looping over the message log and append any changes to the text list
                while (true) {
                    if(getActivity() != null) {
                        FrameLayout mainLayout = (FrameLayout) getActivity().findViewById(R.id.mainFrame);
                        // Get the console text inside the loop as the fragment gets opened and closed
                        if (!ClientListener.instance().getMessageLog().isEmpty()) {
                            // Get the message log from the client listener
                            List<String> messageLog = ClientListener.instance().getMessageLog();
                            if (lastMessageRecieved < messageLog.size()) {

                                // Hold the messages since last ui update to add to the console view
                                final List<String> messagesToAdd = new ArrayList<String>();

                                // Loop from last message to current message log size
                                for(int i=lastMessageRecieved;i<messageLog.size();i++)
                                {
                                    messagesToAdd.add(messageLog.get(i - 1));
                                }

                                // Update the last message recieved
                                lastMessageRecieved = messageLog.size();

                                //final String strLastMessage = messageLog.get(lastMessageRecieved - 1);
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final TableLayout tableConsole = (TableLayout) getActivity().findViewById(R.id.tableConsole);
                                            final ScrollView scrollConsole = (ScrollView) getActivity().findViewById(R.id.scrollConsole);

                                            if (tableConsole != null)
                                            {
                                                for(String messageToAdd : messagesToAdd) {
                                                    // Create a new table row
                                                    TableRow tr = new TableRow(getActivity());
                                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                                            TableRow.LayoutParams.WRAP_CONTENT);
                                                    tr.setLayoutParams(lp);
                                                    // Create the url text view for this row
                                                    TextView textLastMessage = new TextView(getActivity());
                                                    textLastMessage.setLayoutParams(new TableRow.LayoutParams(
                                                            TableRow.LayoutParams.MATCH_PARENT,
                                                            TableRow.LayoutParams.WRAP_CONTENT));
                                                    textLastMessage.setText(messageToAdd);
                                                    tr.addView(textLastMessage);
                                                    // Add the row to the table
                                                    tableConsole.addView(tr);

                                                    scrollConsole.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            scrollConsole.fullScroll(View.FOCUS_DOWN);
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }

                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_console, container, false);
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
