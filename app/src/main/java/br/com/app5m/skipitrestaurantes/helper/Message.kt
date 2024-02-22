package br.com.app5m.skipitrestaurantes.helper

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import br.com.app5m.skipitrestaurantes.R


class Message {
    private var context: Context? = null

    constructor(context: Context?) {
        this.context = context
    }

    constructor() {}

    var alertGps: AlertDialog? = null
//    fun msgGPS(answer: Answer) {
//        val builder = AlertDialog.Builder(
//            context!!
//        )
//        alertGps = builder.create()
//        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view: View = inflater.inflate(R.layout.toast, null)
//        /*Animation logoMoveAnimation = AnimationUtils.loadAnimation(activity, R.anim.toast);
//        view.setAnimation(logoMoveAnimation);*/alertGps!!.setView(view)
//        alertGps!!.window!!.setBackgroundDrawableResource(R.color.transparent1)
//        alertGps!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        alertGps!!.setCanceledOnTouchOutside(false)
//        alertGps!!.setCancelable(false)
//        alertGps!!.show()
//        val titleTxt = view.findViewById<TextView>(R.id.title_txt)
//        val msgTxt = view.findViewById<TextView>(R.id.msg_txt)
//        val confirm = view.findViewById<TextView>(R.id.confirm_btn)
//        val cancel = view.findViewById<TextView>(R.id.cancel_btn)
//        titleTxt.text = "Localização Desativada"
//        msgTxt.text = "Para que o app funcione corretamente ative sua localização."
//        cancel.visibility = View.GONE
//        confirm.text = "Ok"
//        confirm.setOnClickListener { v: View? ->
//            answer.setOnClickListener()
//            alertGps!!.dismiss()
//        }
//    }

    interface Answer {
        fun setOnClickListener()
    }

    interface AnswerTermos {
        fun setOnClickListener(termos: Boolean?)
    }

    companion object {




        fun msg(activity: Context, title: String?, msg: String?, answer: Answer) {
            val builder = AlertDialog.Builder(activity)
            val alertDialog = builder.create()
            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.toast, null)
            //Animation logoMoveAnimation = AnimationUtils.loadAnimation(activity, R.anim.zoom_in);
            //view.setAnimation(logoMoveAnimation);
            alertDialog.setView(view)

            alertDialog.window!!.setBackgroundDrawableResource(R.color.trasparent)
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.setCancelable(false)
            alertDialog.show()
            val confirm = view.findViewById<TextView>(R.id.confirm_btn)
            val cancel = view.findViewById<TextView>(R.id.cancel_btn)
            val titleTxt = view.findViewById<TextView>(R.id.title_txt)
            val msgTxt = view.findViewById<TextView>(R.id.msg_txt)
            titleTxt.text = title
            msgTxt.text = msg
            confirm.setOnClickListener { v: View? ->
                answer.setOnClickListener()
                alertDialog.dismiss()
            }
            cancel.visibility = View.VISIBLE
            cancel.setOnClickListener { v: View? ->
                alertDialog.dismiss()
            }
        }

     /*   fun permissionDenied(activity: Activity, msg: String?, title: String?, answer: Answer) {
            val builder = AlertDialog.Builder(activity)
            val alertDialog = builder.create()
            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.toast, null)
            alertDialog.setView(view)
            alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent1)
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.setCancelable(false)
            alertDialog.show()
            val titleTxt = view.findViewById<TextView>(R.id.title_txt)
            val msgTxt = view.findViewById<TextView>(R.id.msg_txt)
            val confirm = view.findViewById<TextView>(R.id.confirm_btn)
            val cancel = view.findViewById<TextView>(R.id.cancel_btn)
            titleTxt.text = title
            msgTxt.text = msg
            confirm.setOnClickListener { v: View? ->
                answer.setOnClickListener()
                alertDialog.dismiss()
            }
            cancel.visibility = View.GONE
        }*/

        /*public static void offlineMsg(Activity activity, Answer answer){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_offline, null);
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button confirm = view.findViewById(R.id.confirm_btn);

        confirm.setOnClickListener(v -> {
            answer.setOnClickListener();
            alertDialog.dismiss();
        });

    }*/
        /*public static void success(Context activity, String status, String msg, Answer answer){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_offline, null);
        Animation logoMoveAnimation = AnimationUtils.loadAnimation(activity, R.anim.toast);
        view.setAnimation(logoMoveAnimation);
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        TextView confirm = view.findViewById(R.id.confirm_btn);
        ImageView titleTxt = view.findViewById(R.id.title_txt);
        TextView msgTxt = view.findViewById(R.id.msg_txt);

        confirm.setText("Ok");

        if (status.equals("01")){
            titleTxt.setImageDrawable(activity.getDrawable(R.drawable.ic_success));
        }
        msgTxt.setText(msg);

        confirm.setOnClickListener(v -> {
            answer.setOnClickListener();
            alertDialog.dismiss();
        });

    }*/
  /*      fun termos(activity: Activity, answer: AnswerTermos) {
            val builder = AlertDialog.Builder(activity)
            val alertDialog = builder.create()
            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.termos, null)
            //Animation logoMoveAnimation = AnimationUtils.loadAnimation(activity, R.anim.zoom_in);
            //view.setAnimation(logoMoveAnimation);
            alertDialog.setView(view)
            alertDialog.window!!.setBackgroundDrawableResource(R.color.transparent1)
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.setCancelable(false)
            alertDialog.show()
            val confirm = view.findViewById<TextView>(R.id.confirm_btn)
            val cancel = view.findViewById<TextView>(R.id.cancel_btn)
            confirm.setOnClickListener { v: View? ->
                answer.setOnClickListener(true)
                alertDialog.dismiss()
            }
            cancel.setOnClickListener { v: View? ->
                answer.setOnClickListener(false)
                alertDialog.dismiss()
            }
        }*/
    }
}