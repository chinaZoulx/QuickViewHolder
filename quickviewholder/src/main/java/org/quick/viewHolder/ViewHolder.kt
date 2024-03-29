package org.quick.viewHolder

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import org.quick.viewHolder.callback.OnClickListener2

/**
 * 视图持有器
 * @from https://github.com/SpringSmell/QuickViewHolder
 */
open class ViewHolder(var itemView:View):VHService {
    private val mViews: SparseArray<View> by lazy { return@lazy SparseArray<View>() }

   @Suppress("UNCHECKED_CAST")
   override fun <T : View> getView(@IdRes id: Int): T? {

        var view: View? = mViews.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            mViews.put(id, view)
        }
        return view as T?
    }

    override fun setText(@IdRes id: Int, content: CharSequence?, onClickListener: ((view: View, vh: ViewHolder) -> Unit)? ): VHService {
        val textView = getView<TextView>(id)
        textView?.text = content
        if (onClickListener != null) {
            textView?.setOnClickListener (object : OnClickListener2() {
                override fun click(view: View) {
                    onClickListener.invoke(view, this@ViewHolder)
                }
            })
        }
        return this
    }

    /**
     * 原样本地图片
     *
     * @param id
     * @param iconId
     * @return
     */
    override fun setImg(@IdRes id: Int, @DrawableRes iconId: Int, onClickListener: ((view: View, vh: ViewHolder) -> Unit)? ): VHService {
        return setImg(id, false, 0f, "", iconId, onClickListener)
    }

    /**
     * 原样网络图片
     *
     * @param id
     * @param url
     * @return
     */
    override fun setImg(@IdRes id: Int, url: CharSequence, onClickListener: ((view: View, vh: ViewHolder) -> Unit)?): VHService {
        return setImg(id, false, 0f, url, 0, onClickListener)
    }


    /**
     * 圆角-本地图片
     *
     * @param id
     * @param radius
     * @param iconId
     * @return
     */
    override fun setImgRoundRect(@IdRes id: Int, radius: Float, @DrawableRes iconId: Int, onClickListener: ((view: View, vh: ViewHolder) -> Unit)?): VHService {
        return setImg(id, false, radius, "", iconId, onClickListener)
    }

    /**
     * 圆角-网络图片
     *
     * @param id
     * @param radius
     * @param url
     * @return
     */
    override fun setImgRoundRect(@IdRes id: Int, radius: Float, url: CharSequence, onClickListener: ((view: View, vh: ViewHolder) -> Unit)?): VHService {
        return setImg(id, false, radius, url, 0, onClickListener)
    }


    /**
     * 圆形-网络图片
     *
     * @param id
     * @param url
     * @param onClickListener
     * @return
     */
    override fun setImgCircle(@IdRes id: Int, url: CharSequence, onClickListener: ((view: View, vh: ViewHolder) -> Unit)? ): VHService {
        return setImg(id, true, 0f, url, 0, onClickListener)
    }

    /**
     * 圆形-本地图片
     *
     */
    override  fun setImgCircle(@IdRes id: Int, @DrawableRes imgRes: Int, onClickListener: ((view: View, vh: ViewHolder) -> Unit)?): VHService {
        return setImg(id, true, 0f, "", imgRes, onClickListener)
    }

    /**
     * @param id              图片ID
     * @param isCir           是否正圆
     * @param radius          圆角
     * @param url             网络链接
     * @param imgRes          图片资源ID
     * @param onClickListener 监听
     * @return
     */
    @Synchronized
    private fun setImg(id: Int, isCir: Boolean, radius: Float, url: CharSequence, @DrawableRes imgRes: Int, onClickListener: ((view: View, vh: ViewHolder) -> Unit)?): VHService {

        val img = getView<ImageView>(id)
        if (TextUtils.isEmpty(url)) {
            when {
                isCir -> img?.setImageBitmap(Utils.cropCircle(Utils.decodeSampledBitmapFromResource(itemView.context,itemView.context.resources, imgRes, img.measuredWidth, img.measuredHeight)))
                radius > 0 -> img?.setImageBitmap(Utils.cropRoundRect(Utils.decodeSampledBitmapFromResource(itemView.context,itemView.context.resources, imgRes, img.measuredWidth, img.measuredHeight), radius))
                else -> img?.setImageResource(imgRes)
            }
        } else {
            when {
                isCir -> bindImgCircle(itemView.context, url.toString(), img)
                radius > 0 -> bindImgRoundRect(itemView.context, url.toString(), radius, img)
                else -> bindImg(itemView.context, url.toString(), img)
            }
        }
        if (onClickListener != null) img?.setOnClickListener (object : OnClickListener2() {
            override fun click(view: View) {
                onClickListener.invoke(view, this@ViewHolder)
            }
        })
        return this
    }

    override fun bindImgCircle(context: Context, url: String, imageView: ImageView?): VHService {
        return this
    }

    override fun bindImg(context: Context, url: String, imageView: ImageView?): VHService {
        return this
    }

    override fun bindImgRoundRect(context: Context, url: String, radius: Float, imageView: ImageView?): VHService {
        return this
    }

    override fun setOnClick(onClickListener: (view: View, vh: ViewHolder) -> Unit, @IdRes vararg ids: Int): VHService {
        for (id in ids) {
            getView<View>(id)?.setOnClickListener(object : OnClickListener2() {
                override fun click(view: View) {
                    onClickListener.invoke(view, this@ViewHolder)
                }
            })
        }
        return this
    }

    override fun setProgress(@IdRes id: Int, value: Int): VHService {
        getView<ProgressBar>(id)?.progress = value
        return this
    }

    override fun setCheck(@IdRes id: Int, isChecked: Boolean): VHService {
        getView<CompoundButton>(id)?.isChecked = isChecked
        return this
    }

    override fun setBackgroundResource(@IdRes id: Int, bgResId: Int): VHService {
        getView<View>(id)?.setBackgroundResource(bgResId)
        return this
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override  fun setBackground(@IdRes id: Int, background: Drawable): VHService {
        getView<View>(id)?.background = background
        return this
    }

    override fun setBackgroundColor(@IdRes id: Int, background: Int): VHService {
        getView<View>(id)?.setBackgroundColor(background)
        return this
    }

    override fun setVisibility(visibility: Int, @NonNull @IdRes vararg resIds: Int): VHService {
        for (resId in resIds) getView<View>(resId)?.visibility = visibility
        return this
    }

    override fun getTextView(@IdRes id: Int): TextView? {
        return getView(id)
    }

    override fun getButton(@IdRes id: Int): Button? {
        return getView(id)
    }

    override  fun getImageView(@IdRes id: Int): ImageView? {
        return getView(id)
    }

    override fun getLinearLayout(@IdRes id: Int): LinearLayout? {
        return getView(id)
    }

    override fun getRelativeLayout(@IdRes id: Int): RelativeLayout? {
        return getView(id)
    }

    override fun getFramLayout(@IdRes id: Int): FrameLayout? {
        return getView(id)
    }

    override  fun getCheckBox(@IdRes id: Int): CheckBox? {
        return getView(id)
    }

    override fun getEditText(@IdRes id: Int): EditText? {
        return getView(id)
    }
}