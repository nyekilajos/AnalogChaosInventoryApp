package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Fragment for the navigation drawer
 *
 * @author Lajos Nyeki
 */
public class NavigationDrawerFragment extends RoboFragment {

    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();

    @InjectView(R.id.drawerList)
    private RecyclerView recyclerView;

    @InjectView(R.id.nav_header_container)
    private LinearLayout navigationDrawerHeader;

    @Inject
    private NavigationDrawerAdapter navigationDrawerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(navigationDrawerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        navigationDrawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing
            }
        });
    }

    public void setDrawerItemSelectedListener(DrawerItemSelectedListener listener) {
        recyclerView.addOnItemTouchListener(new DrawerTouchListener(getActivity(), listener));
    }

    private class DrawerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetectorCompat gestureDetector;
        private DrawerItemSelectedListener listener;

        public DrawerTouchListener(Context context, DrawerItemSelectedListener listener) {
            this.listener = listener;
            gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && listener != null && gestureDetector.onTouchEvent(e)) {
                listener.onDrawerItemSelected(child, recyclerView.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }

    public interface DrawerItemSelectedListener {
        void onDrawerItemSelected(View view, int position);
    }


}
