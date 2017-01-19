package bboat.boatwrightatmbranchlocator.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import bboat.boatwrightatmbranchlocator.R;
import bboat.boatwrightatmbranchlocator.datamodel.ChaseLocation;


public class ChaseLocationDetailsDialogFragment extends DialogFragment implements View.OnClickListener {
    private ChaseLocation mChaseLocation;

    public ChaseLocationDetailsDialogFragment() {
        // Required empty public constructor
    }

    public static ChaseLocationDetailsDialogFragment newInstance(ChaseLocation chaseLocation) {
        ChaseLocationDetailsDialogFragment fragment = new ChaseLocationDetailsDialogFragment();
        fragment.mChaseLocation = chaseLocation;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        switch (mChaseLocation.getLoctype()) {
            case "atm":
                view = inflater.inflate(R.layout.fragment_chase_location_details_atm, container, false);
                setupAtmFields(view);
                break;
            case "branch":
                view = inflater.inflate(R.layout.fragment_chase_location_details_branch, container, false);
                setupBranchFields(view);
                break;
            default:
                dismiss();
                return null;
        }

        setupSharedFields(view);
        return view;
    }

    private void setupSharedFields(View view) {
        Button done = (Button) view.findViewById(R.id.cld_btn_done);
        done.setOnClickListener(this);

        TextView name = (TextView) view.findViewById(R.id.cld_name);
        name.setText(mChaseLocation.getName());

        TextView address = (TextView) view.findViewById(R.id.cld_address);
        address.setText(String.format("%s\n%s, %s\n%s",
                mChaseLocation.getAddress(),
                mChaseLocation.getCity(),
                mChaseLocation.getState(),
                mChaseLocation.getZip()));

        TextView distance = (TextView) view.findViewById(R.id.cld_distance);
        distance.setText(String.valueOf(mChaseLocation.getDistance()));

    }

    private void setupBranchFields(View view) {
        TextView phone = (TextView) view.findViewById(R.id.cld_phone);
        phone.setText(mChaseLocation.getPhone()
                .replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3"));

        TextView atms = (TextView) view.findViewById(R.id.cld_atms);
        atms.setText(String.valueOf(mChaseLocation.getAtms()));

        TextView lobby = (TextView) view.findViewById(R.id.cld_lobby);
        List<String> lobbyHours = mChaseLocation.getLobbyhrs();
        lobby.setText(String.valueOf(getFormattedStringFromList(lobbyHours)));

        TextView driveUp = (TextView) view.findViewById(R.id.cld_du_hours);
        List<String> duHours = mChaseLocation.getDriveuphrs();
        driveUp.setText(String.valueOf(getFormattedStringFromList(duHours)));

        TextView type = (TextView) view.findViewById(R.id.cld_type);
        type.setText(String.valueOf(mChaseLocation.getType()));
    }

    private void setupAtmFields(View view) {
        TextView access = (TextView) view.findViewById(R.id.cld_access);
        access.setText(String.valueOf(mChaseLocation.getAccess()));

        TextView services = (TextView) view.findViewById(R.id.cld_services);
        List<String> serviceTypes = mChaseLocation.getServices();
        services.setText(String.valueOf(getFormattedStringFromList(serviceTypes)));
    }

    public String getFormattedStringFromList(List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            String singleLine = list.get(i);
            sb.append(TextUtils.isEmpty(singleLine) ? "Closed" : singleLine.replace("-", " - "));

            if (i < list.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cld_btn_done:
                dismiss();
        }
    }
}
