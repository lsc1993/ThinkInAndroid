package com.ls.storage.ui

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ls.storage.databinding.FragmentStoragePageLayoutBinding
import java.io.File

class StorageFragment : Fragment() {

    private lateinit var root: FragmentStoragePageLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = FragmentStoragePageLayoutBinding.inflate(inflater, container, false)
        return root.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        root.tvOsVersion.text = "Android ${Build.VERSION.RELEASE}"
        root.tvSDKVersion.text = "Android SDK ${Build.VERSION.SDK_INT}"

        root.tvAppInternal.text =
            String.format(INTERNAL_FORMAT, requireContext().filesDir, requireContext().cacheDir)
        root.tvAppExternal.text =
            String.format(
                EXTERNAL_FORMAT,
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM),
                requireContext().externalCacheDir
            )

        root.btnCreateExternal.setOnClickListener {
            val path = requireContext().getExternalFilesDir(null)
            val filename = "ls_storage.txt"
            val file = File(path, filename)
            if (file.exists()) {
                Toast.makeText(requireContext(), "文件已存在", Toast.LENGTH_SHORT).show()
            } else {
                val result = file.createNewFile()
                if (result) {
                    root.tvExternalFile.text = "文件创建成功: ${file.absolutePath}"
                }
            }
        }

        root.btnReadExternal.setOnClickListener {

        }

        root.btnDelExternal.setOnClickListener {
            val path = requireContext().getExternalFilesDir(null)
            val filename = "ls_storage.txt"
            val file = File(path, filename)
            if (file.exists()) {
                val result = file.delete()
                if (result) {
                    root.tvExternalFile.text = "文件删除成功"
                }
            } else {
                Toast.makeText(requireContext(), "文件不存在", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val INTERNAL_FORMAT = "getFilesDir(): %s\ngetCacheDir(): %s"
        const val EXTERNAL_FORMAT = "getExternalFilesDir(): %s\ngetExternalCacheDir(): %s"
    }
}