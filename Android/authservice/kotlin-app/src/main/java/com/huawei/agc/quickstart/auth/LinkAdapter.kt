/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.agc.quickstart.auth

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.huawei.agc.quickstart.auth.AuthMainActivity.LinkClickCallback

class LinkAdapter(context: Context, private val resourceId: Int, private val list: List<LinkEntity>, private val linkClickCallback: LinkClickCallback?) : ArrayAdapter<LinkEntity?>(context, resourceId) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val linkEntity = getItem(position)
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.imageView = view.findViewById(R.id.image)
            viewHolder.textView = view.findViewById(R.id.name)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val resourceId: Int? = linkEntity?.resourceId
        if (resourceId != null) {
            viewHolder.imageView?.setImageResource(resourceId)
        }

        viewHolder.textView?.setText(linkEntity?.name)
        view.setOnClickListener {
            val intent = Intent(context, linkEntity?.activity)
            intent.putExtra("link", true)
            context.startActivity(intent)
            linkClickCallback?.click()
        }
        return view
    }

    override fun getItem(position: Int): LinkEntity? {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    internal inner class ViewHolder {
        var imageView: ImageView? = null
        var textView: TextView? = null
    }

}