package com.example.myapplication.ui.Register
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedButton: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonperson.setOnClickListener { openFileChooser(binding.buttonperson) }
        binding.Adharcardfront.setOnClickListener { openFileChooser(binding.Adharcardfront) }
        binding.Adharcardback.setOnClickListener { openFileChooser(binding.Adharcardback) }
        binding.Witnessadharcardfront.setOnClickListener { openFileChooser(binding.Witnessadharcardfront) }
        binding.Witnessadharcardback.setOnClickListener { openFileChooser(binding.Witnessadharcardback) }

        binding.buttonSubmit.setOnClickListener { uploadData() }
    }

    private fun openFileChooser(button: View) {
        selectedButton = button
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            when (selectedButton.id) {
                R.id.buttonperson -> binding.buttonperson.text = "Image Selected"
                R.id.Adharcardfront -> binding.Adharcardfront.text = "Image Selected"
                R.id.Adharcardback -> binding.Adharcardback.text = "Image Selected"
                R.id.Witnessadharcardfront -> binding.Witnessadharcardfront.text = "Image Selected"
                R.id.Witnessadharcardback -> binding.Witnessadharcardback.text = "Image Selected"
            }
        }
    }

    private fun uploadData() {
        if (imageUri != null) {
            val storageReference = FirebaseStorage.getInstance().getReference("uploads/" + UUID.randomUUID().toString())
            storageReference.putFile(imageUri!!)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        saveDataToDatabase(uri.toString())
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            saveDataToDatabase(null)
        }
    }

    private fun saveDataToDatabase(imageUrl: String?) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("registrations")
        val registrationId = databaseReference.push().key

        val registration = Registration(
            name = binding.editTextName.text.toString().trim(),
            age = binding.editTextAge.text.toString().trim(),
            address = binding.editTextAddress.text.toString().trim(),
            adharCard = binding.editTextAdharCard.text.toString().trim(),
            mobile = binding.editTextMobile.text.toString().trim(),
            sickness = binding.editTextsick.text.toString().trim(),
            childInfo = binding.editTextchildinfo.text.toString().trim(),
            childMobileNum = binding.editTextchildmobilenum.text.toString().trim(),
            medicine = binding.editTextMedicine.text.toString().trim(),
            witnessName = binding.edittextWitnessName.text.toString().trim(),
            witnessAdharCard = binding.editTextWitnessAdharCard.text.toString().trim(),
            witnessMobile = binding.editTextWitnessMobile.text.toString().trim(),
            imageUrl = imageUrl
        )

        if (registrationId != null) {
            databaseReference.child(registrationId).setValue(registration)
                .addOnCompleteListener {
                    Toast.makeText(requireContext(), "Data submitted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Submission failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class Registration(
        val name: String,
        val age: String,
        val address: String,
        val adharCard: String,
        val mobile: String,
        val sickness: String,
        val childInfo: String,
        val childMobileNum: String,
        val medicine: String,
        val witnessName: String,
        val witnessAdharCard: String,
        val witnessMobile: String,
        val imageUrl: String?
    )
}
