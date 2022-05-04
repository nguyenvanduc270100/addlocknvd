# AppLocker

## Các chú ý chung về Base

- Sử dụng kotlin làm ngôn ngữ chính.
- Bắt buộc sử dụng View Binding thay thế cho findViewById trong gần như tất cả các trường hợp. Tham khảo về View Binding: 
[https://developer.android.com/topic/libraries/view-binding](https://developer.android.com/topic/libraries/view-binding)
- Các activity phải kế thừa từ `BaseActivity`, sử dụng kết hợp với View Binding. Ví dụ
```kotlin
package com.bienlongtuan.applocker.activities

import android.os.Bundle
import com.bienlongtuan.applocker.databinding.ActivityMainBinding // <-- tự gen theo layout
import com.bienlongtuan.applocker.fragments.BlankFragment

class MainActivity : BaseActivity<ActivityMainBinding>() { // <-- Khai báo kế thừa và kiểu dữ liệu của View Binding
    override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater) // <-- Khởi tạo View Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.appCompatTextView.text = "Lmao" // <-- binding.<id của view muốn thao tác trong layout>

        addFragmentToView(
            BlankFragment(),
            binding.viewFragmentContainer
        )
    }
}
```
- Các Fragment phải kế thừa từ `BaseFragment`, sử dụng kết hợp với View Binding. Ví dụ
```kotlin
package com.bienlongtuan.applocker.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bienlongtuan.applocker.databinding.FragmentBlankBinding

class BlankFragment : BaseFragment<FragmentBlankBinding>() { // <-- Khai báo kế thừa và kiểu dữ liệu của View Binding
    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBlankBinding {
        return FragmentBlankBinding.inflate(layoutInflater, container, false) // <-- Khởi tạo View Binding
    }

    override fun onCreateOurView() {
        binding.tvTest.text = "Trầm cảm lên"
    }
}
```
- Layout của các Activity phải luôn có cha là `com.bienlongtuan.applocker.layouts.SafeConstraintLayout`. Ví dụ với `activity_main`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.bienlongtuan.applocker.layouts.SafeConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".activities.MainActivity">

    <androidx.appcompat.widget.AppCompatTextView
        android:id="@+id/appCompatTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <FrameLayout
        android:id="@+id/viewFragmentContainer"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/appCompatTextView" />

</com.bienlongtuan.applocker.layouts.SafeConstraintLayout>

```

- Theme của app nói chung hay của các activity nói riêng sẽ là `@style/Theme.AppLocker`
- Có rất nhiều lớp Utils được tích hợp để giúp mình giải quyết rất nhiều vấn đề. Tham khảo thêm tại: [https://github.com/Blankj/AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

## Coding Conventions
### CODING
- Comment đầu file: tên file (class), người viết, ngày viết
- Comment đầu hàm: chức năng hàm
- Trong một hàm mà có đoạn code nào xử lý phức tạp, khó hiểu thì cũng có thể comment
- Một hàm dài không quá 30 lines code
- Trong một hàm không có for lồng nhau
- Member property của class thì thêm tiền tố 'm', ex: mInstance
- Function theo kiểu android: chữ bắt đầu không viết hoa, ex: fun getUserName()
### FIX BUG
- Comment: tên issue, ngày fix, người fix trước và sau đoạn sửa

