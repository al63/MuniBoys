package ride.the.bus.muniboys.predictions

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ride.the.bus.muniboys.R

class BusActivity: AppCompatActivity() {

    lateinit var mPresenter: BusPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bus)
        mPresenter = BusPresenter.create(BusViewDelegate.create(this, findViewById(R.id.root)))
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }
}
