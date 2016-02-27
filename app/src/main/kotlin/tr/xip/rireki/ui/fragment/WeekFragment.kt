package tr.xip.rireki.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tr.xip.rireki.R

class WeekFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_week, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // TODO: Stuff
    }
}