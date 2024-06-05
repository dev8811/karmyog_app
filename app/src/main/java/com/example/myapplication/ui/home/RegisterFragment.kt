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
import com.example.myapplication.ui.home.HomeFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var personImageUri: Uri? = null
    private var adharFrontImageUri: Uri? = null
    private var adharBackImageUri: Uri? = null
    private var witnessFrontImageUri: Uri? = null
    private var witnessBackImageUri: Uri? = null

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
            when (selectedButton.id) {
                R.id.buttonperson -> {
                    personImageUri = data.data
                    binding.buttonperson.text = "Image Selected"
                }

                R.id.Adharcardfront -> {
                    adharFrontImageUri = data.data
                    binding.Adharcardfront.text = "Image Selected"
                }

                R.id.Adharcardback -> {
                    adharBackImageUri = data.data
                    binding.Adharcardback.text = "Image Selected"
                }

                R.id.Witnessadharcardfront -> {
                    witnessFrontImageUri = data.data
                    binding.Witnessadharcardfront.text = "Image Selected"
                }

                R.id.Witnessadharcardback -> {
                    witnessBackImageUri = data.data
                    binding.Witnessadharcardback.text = "Image Selected"
                }
            }
        }
    }

    private fun uploadData() {
        if (validateFields() && areImagesSelected()) {
            val imageUris = listOf(
                personImageUri,
                adharFrontImageUri,
                adharBackImageUri,
                witnessFrontImageUri,
                witnessBackImageUri
            )
            val imageUrls = mutableListOf<String?>()

            uploadImages(imageUris, imageUrls, 0)
        }
    }

    private fun areImagesSelected(): Boolean {
        if (personImageUri == null || adharFrontImageUri == null || adharBackImageUri == null ||
            witnessFrontImageUri == null || witnessBackImageUri == null
        ) {
            Toast.makeText(requireContext(), "Please select all images", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateFields(): Boolean {
        val name = binding.editTextName.text.toString().trim()
        val age = binding.editTextAge.text.toString().trim()
        val address = binding.editTextAddress.text.toString().trim()
        val adharCard = binding.editTextAdharCard.text.toString().trim()
        val mobile = binding.editTextMobile.text.toString().trim()
        val sickness = binding.editTextsick.text.toString().trim()
        val childInfo = binding.editTextchildinfo.text.toString().trim()
        val childMobileNum = binding.editTextchildmobilenum.text.toString().trim()
        val medicine = binding.editTextMedicine.text.toString().trim()
        val witnessName = binding.edittextWitnessName.text.toString().trim()
        val witnessAdharCard = binding.editTextWitnessAdharCard.text.toString().trim()
        val witnessMobile = binding.editTextWitnessMobile.text.toString().trim()

        // Check if any field is empty
        if (name.isEmpty() || age.isEmpty() || address.isEmpty() || adharCard.isEmpty() ||
            mobile.isEmpty() || sickness.isEmpty() || childInfo.isEmpty() || childMobileNum.isEmpty() ||
            medicine.isEmpty() || witnessName.isEmpty() || witnessAdharCard.isEmpty() || witnessMobile.isEmpty()
        ) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check mobile number format
        if (!isValidMobileNumber(mobile)) {
            Toast.makeText(requireContext(), "Invalid mobile number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidMobileNumber(witnessMobile)) {
            Toast.makeText(requireContext(), "Invalid witness mobile number", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        // Check age format
        if (!isValidAge(age)) {
            Toast.makeText(
                requireContext(),
                "Invalid age (must be between 60 and 100)",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // Check Aadhar Card number format
        if (!isValidAadharCardNumber(adharCard)) {
            Toast.makeText(requireContext(), "Invalid Aadhar Card number", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        // Check Witness Aadhar Card number format
        if (!isValidAadharCardNumber(witnessAdharCard)) {
            Toast.makeText(
                requireContext(),
                "Invalid Witness Aadhar Card number",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // Check if Aadhar card numbers and mobile numbers are not the same
        if (adharCard == witnessAdharCard) {
            Toast.makeText(
                requireContext(),
                "Aadhar card numbers cannot be the same",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (mobile == witnessMobile) {
            Toast.makeText(
                requireContext(),
                "Mobile numbers cannot be the same",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // Check if any image is not selected
        if (personImageUri == null || adharFrontImageUri == null || adharBackImageUri == null ||
            witnessFrontImageUri == null || witnessBackImageUri == null
        ) {
            Toast.makeText(requireContext(), "All images must be selected", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }

    private fun isValidMobileNumber(mobile: String): Boolean {
        // Mobile number should have exactly 10 digits
        return mobile.length == 10 && mobile.all { it.isDigit() }
    }

    private fun isValidAge(age: String): Boolean {
        // Age should be a positive integer between 60 and 100
        val ageInt = age.toIntOrNull()
        return ageInt != null && ageInt in 60..100
    }

    private fun isValidAadharCardNumber(aadharCard: String): Boolean {
        // Aadhar Card number should have exactly 12 digits and contain only numbers
        return aadharCard.length == 12 && aadharCard.all { it.isDigit() }
    }


    private fun uploadImages(imageUris: List<Uri?>, imageUrls: MutableList<String?>, index: Int) {
        if (index < imageUris.size) {
            val uri = imageUris[index]
            if (uri != null) {
                val storageReference = FirebaseStorage.getInstance()
                    .getReference("uploads/" + UUID.randomUUID().toString())
                storageReference.putFile(uri)
                    .addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                            imageUrls.add(downloadUri.toString())
                            uploadImages(imageUris, imageUrls, index + 1)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Upload failed: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                imageUrls.add(null)
                uploadImages(imageUris, imageUrls, index + 1)
            }
        } else {
            saveDataToDatabase(imageUrls)
        }
    }

    private fun saveDataToDatabase(imageUrls: List<String?>) {
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
            personImageUrl = imageUrls.getOrNull(0),
            adharFrontImageUrl = imageUrls.getOrNull(1),
            adharBackImageUrl = imageUrls.getOrNull(2),
            witnessFrontImageUrl = imageUrls.getOrNull(3),
            witnessBackImageUrl = imageUrls.getOrNull(4),


            )

        if (registrationId != null) {
            databaseReference.child(registrationId).setValue(registration)
                .addOnCompleteListener {
                    Toast.makeText(
                        requireContext(),
                        "Data submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val registerFragment = HomeFragment()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.fragment_container,
                        registerFragment
                    ) // Use the ID of your container
                    transaction.addToBackStack(null) // Optional: add to back stack to allow back navigation
                    transaction.commit()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Submission failed: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
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
        val personImageUrl: String?,
        val adharFrontImageUrl: String?,
        val adharBackImageUrl: String?,
        val witnessFrontImageUrl: String?,
        val witnessBackImageUrl: String?
    )
}
