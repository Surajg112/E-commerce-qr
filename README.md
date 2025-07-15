# FullStackRetail

Ecommerce Backend
Software Required
Java 17
MySQL 8 & SQLYog
MongoDB 6 & MongoDB Compass
IntelliJ Idea
ZipKin Server

Create new folder- Ex- RetailEcommerce



Steps To Start Projects
Open all projects in IntelliJ Idea

Build the all projects using maven :- mvn clean install

First run ServiceRegistry

Then run Wishlistservice , OrderService , CartService , ApiGateway , UserService

Note: If you PostgreSQL in your system then change spring profile test to dev in application.yml for CartService & WishlistService and create database accordingly

For ProductService please open this project in new window

Note: If you open in same window for save & update product functionality it give 404 exception - File Not Found

Please download Zipkin Server from here. Paste the zipkin-server-2.23.19-exec file in ECommerceApp-Backend folder. Open cmd from ECommerceApp-Backend location type java -jar zipkin-server-2.23.19-exec enter

Important URL'S
Service Registry Port - 8761 :- Eureka Server

API Gateway Port - 9000 :-

Note: For Gateway API Documentation Please Use Postman

User Service Port - 9001 :- API Documentation For User Service

Product Service Port - 9002 :- API Documentation For Product Service

Cart Service Port - 9003 :- API Documentation For Cart Service

Order Service Port - 9004 :- API Documentation For Order Service

Wishlist Service Port - 9005 :- API Documentation For Wishlist Service

Hystrix Dashboard Port - 9009 :- Hystrix Dashboard

Zipkin Server Port - 9411 :- Zipkin Server

Admin Server Port - 8761 :- Spring Boot Admin Server



Front End-
Software Required
Node JS
VS Code
Steps To Start Project
Open this project in VS Code

Open new terminal shortcut key -

Ctrl + `
and type npm install + press Enter

To start application type npm start + press Enter to start project

Note: Please note that server running on http://localhost:8000

Steps To Creating Admin User
First do Signup

After successfull sign up please open MongoDB Compass -> open connection -> open UserService Database -> open UserInfo -> double click on "USER" -> change it into ADMIN
(it looks like in db=> roles: "ADMIN") -> click on update buttton

Signin using this details

Note: Please note that UserName is your EmailId

Steps To Add New Product
Signin using Admin user
Click on Dashboard -> Click Manage Category -> Add new category -> save
Click on Dashboard -> Click Manage Sub Category -> Add new subcategory -> save
Click on Dashboard -> Click Manage Product -> Add product -> save
For Creating New Coupon
Signin using admin user
Click on Dashboard -> Manage Coupon -> Add Coupon -> save
Create New User
Sign Up
Sign In


import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  Image,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  Alert,
  ScrollView,
  Platform,
  SafeAreaView,
  KeyboardAvoidingView,
  TouchableWithoutFeedback,
  Keyboard,
  Dimensions,
} from "react-native";
import { useLocalSearchParams, useRouter } from "expo-router";
import { IMGurl, url } from "@/constants";
import { useAuth } from "@/context/AuthContext";
import { MaterialIcons } from "@expo/vector-icons";
type MeasurementsMap = Record<string, string>;

const { width, height } = Dimensions.get("window");

const DigitalTwinPreviewScreen = () => {
  const { data } = useLocalSearchParams();
  const router = useRouter();
  const { token, user } = useAuth();
  const [previewImageUrl, setPreviewImageUrl] = useState<string | null>(null);

  const parsedData = Array.isArray(data) ? data[0] : data;
  const parsed = parsedData ? JSON.parse(decodeURIComponent(parsedData)) : null;

  const [editing, setEditing] = useState(false);

  const [measurements, setMeasurements] = useState<MeasurementsMap>(
  Object.fromEntries(
    Object.entries(parsed?.measurements || {}).map(([k, v]) => [
      k,
      String(v ?? ""),
    ])
  )
);

  const [inputErrors, setInputErrors] = useState<MeasurementsMap>({});

  // Fetch preview image from server

  useEffect(() => {
    if (!user?.userId) {
      console.log("⛔ No user.userId found, skipping image fetch");
      return;
    }

    const fetchPreviewImage = async () => {
      try {
        console.log("⏳ Waiting 1 second before fetching image...");
        await new Promise((resolve) => setTimeout(resolve, 1000)); // 1s delay

        const previewUrl = `${url}/img/getFrontTwinImage?userId=${user.userId}`;
        console.log("📤 Fetching preview image from:-------------", previewUrl);

        const response = await fetch(
          `${url}/img/getFrontTwinImage?userId=${user.userId}`,
          {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const result = await response.json();
        console.log("📥 API response:", result);

        if (response.ok && result.flag) {
          const serverImageUrl = result.data?.imageUrl;
          console.log("✅ Server returned image path:", serverImageUrl);

          let finalImageUrl = serverImageUrl?.startsWith("http")
            ? serverImageUrl
            : `${IMGurl}/${serverImageUrl?.replace(/^\/+/, "")}`;

          setPreviewImageUrl(finalImageUrl);
          console.log("✅ Final preview image URL →", finalImageUrl);
        } else {
          console.log("❌ Preview image fetch failed:", result.message);
        }
      } catch (error) {
        console.error("🚨 Error fetching preview image:", error);
      }
    };

    fetchPreviewImage();
  }, [user?.userId]);

  const handleSave = () => {
    const invalidFields: string[] = [];

    // Check for empty or invalid fields
    Object.entries(measurements).forEach(([key, value]) => {
      const trimmed = String(value).trim();
      const numValue = parseFloat(trimmed);

      if (!trimmed || isNaN(numValue)) {
        invalidFields.push(key);
      }
    });

    if (invalidFields.length > 0) {
      Alert.alert(
        "Measurements Required",
        `Enter valid numeric values for: ${invalidFields.join(", ")}`
      );
      return;
    }

    Alert.alert(
      "Confirm Save",
      "Are you sure you want to save the updated measurements?",
      [
        { text: "No", style: "cancel" },
        {
          text: "Yes",
          onPress: async () => {
            try {
              const cleanedMeasurements = Object.fromEntries(
                Object.entries(measurements).map(([k, v]) => [
                  k,
                  parseFloat(v as string),
                ])
              );

              const payload = {
                status: "twin creation completed",
                measurements: cleanedMeasurements,
              };

              const response = await fetch(
                `${url}/measurements/saveMeasurements`,
                {
                  method: "POST",
                  headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                  },
                  body: JSON.stringify(payload),
                }
              );

              const json = await response.json();

              if (!response.ok || !json.flag) {
                Alert.alert(
                  "Error",
                  json.message || "Failed to save measurements"
                );
                return;
              }

              router.replace("/(drawer)/(tabs)/home");
            } catch (error) {
              Alert.alert("Error", "Something went wrong while saving.");
            }
          },
        },
      ],
      { cancelable: true }
    );
  };

  const measurementOrder = [
    "height",
    "shoulder width",
    "arm length",
    "chest",
    "belly",
    "waist",
    "hips",
    "thigh",
  ];
  const excludedKeys = ["wrist", "ankle", "neck"];
  const sortedMeasurements = Object.entries(measurements)
    .filter(([key]) => !excludedKeys.includes(key.toLowerCase()))
    .sort(
      ([a], [b]) =>
        measurementOrder.indexOf(a.toLowerCase()) -
        measurementOrder.indexOf(b.toLowerCase())
    );
  const hasValidationErrors =
    Object.values(inputErrors).some((msg) => msg) ||
    Object.entries(measurements).some(([key, value]) => {
      const stringValue = String(value);
      return (
        key !== "height" &&
        (stringValue.trim() === "" || isNaN(parseFloat(stringValue)))
      );
    });

  return (
    <SafeAreaView style={styles.safeArea}>
      <Text style={styles.heading}> My Digital Twin </Text>

      <View style={styles.mainContent}>
        <View style={styles.contentRow}>
          {/* Left - Image */}
          <View style={styles.imageWrapper}>
            {previewImageUrl && (
              <Image
                source={{ uri: previewImageUrl }}
                style={styles.image}
                resizeMode="cover"
                onError={(err) =>
                  console.log("❌ Image failed to load:", err.nativeEvent)
                }
              />
            )}
          </View>

          {/* Right - Measurements */}
          <KeyboardAvoidingView
            behavior={Platform.OS === "ios" ? "padding" : "height"}
            style={styles.rightSection}
          >
            <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
              <ScrollView
                contentContainerStyle={styles.scrollContent}
                keyboardShouldPersistTaps="handled"
                showsVerticalScrollIndicator={false}
              >
                <View style={styles.measurementSection}>
                  <Text style={styles.subheading}>Measurements (in cm)</Text>
                  <View style={styles.measurementListContainer}>
                    {sortedMeasurements.map(([key, value]) => (
                      <View key={key} style={styles.measureItem}>
                        <Text style={styles.measureLabelNew}>
                          {key.charAt(0).toUpperCase() + key.slice(1)}
                        </Text>
                        <View style={styles.measureValueWrapperNew}>
                          {editing && key !== "height" ? (
                            <>
                              <TextInput
                                style={[
                                  styles.inputNew,
                                  inputErrors[key]
                                    ? { borderColor: "red", borderWidth: 1 }
                                    : {},
                                ]}
                                keyboardType="decimal-pad"
                                value={String(value)}
                                onChangeText={(text) => {
                                  const raw = text.replace(/[^0-9.]/g, "");

                                  // Prevent multiple decimals
                                  const parts = raw.split(".");
                                  if (parts.length > 2) return;

                                  const [beforeDecimal, afterDecimal = ""] =
                                    parts;

                                  // Limit 3 digits before decimal
                                  if (beforeDecimal.length > 3) return;

                                  // Limit 1 digit after decimal
                                  if (afterDecimal.length > 1) return;

                                  const formatted = parts.join(".");

                                  setMeasurements((prev: MeasurementsMap) => ({
                                    ...prev,
                                    [key]: formatted,
                                  }));

                                  if (formatted === "") {
                                    setInputErrors((prev: MeasurementsMap) => ({
                                      ...prev,
                                      [key]: "Value is required",
                                    }));
                                  } else {
                                    setInputErrors((prev: MeasurementsMap) => ({
                                      ...prev,
                                      [key]: "",
                                    }));
                                  }
                                }}
                                onBlur={() => {
                                  const input = String(measurements[key]);
                                  const numValue = parseFloat(input);

                                  if (!input || isNaN(numValue)) {
                                    setInputErrors((prev: MeasurementsMap) => ({
                                      ...prev,
                                      [key]: "Value is required",
                                    }));
                                    return;
                                  }

                                  const [intPart, decimalPart] =
                                    input.split(".");
                                  if (intPart.length > 3) {
                                    setInputErrors((prev: MeasurementsMap) => ({
                                      ...prev,
                                      [key]:
                                        "Only up to 3 digits allowed before decimal",
                                    }));
                                    return;
                                  }

                                  if (decimalPart?.length > 1) {
                                    setInputErrors((prev) => ({
                                      ...prev,
                                      [key]:
                                        "Only 1 digit allowed after decimal",
                                    }));
                                    return;
                                  }

                                  setInputErrors((prev) => ({
                                    ...prev,
                                    [key]: "",
                                  }));

                                  setMeasurements((prev) => ({
                                    ...prev,
                                    [key]: numValue.toFixed(1),
                                  }));
                                }}
                              />
                              {inputErrors[key] ? (
                                <Text
                                  style={{
                                    color: "red",
                                    fontSize: 12,
                                    marginTop: 4,
                                  }}
                                >
                                  {inputErrors[key]}
                                </Text>
                              ) : null}
                            </>
                          ) : (
                            <Text
                              style={styles.measureTextNew}
                              numberOfLines={1}
                            >
                              {/* {!isNaN(parseFloat(String(value.trim())))
                                ? parseFloat(String(value).trim()).toFixed(1)
                                : "--"} */}

                              {/* {!isNaN(parseFloat(value.trim()))
                                ? parseFloat(value.trim()).toFixed(1)
                                : "--"} */}

                              {(() => {
                                const parsed = parseFloat(
                                  String(value ?? "").trim()
                                );
                                return !isNaN(parsed)
                                  ? parsed.toFixed(1)
                                  : "--";
                              })()}
                            </Text>
                          )}
                        </View>
                      </View>
                    ))}
                  </View>
                </View>
              </ScrollView>
            </TouchableWithoutFeedback>
          </KeyboardAvoidingView>
        </View>

        {/* 🔽 Bottom Buttons */}
        <View style={styles.bottomButtonContainer}>
          <TouchableOpacity
            style={[
              styles.button,
              styles.editButton,
              editing && hasValidationErrors && { opacity: 0.5 },
            ]}
            disabled={editing && hasValidationErrors}
            onPress={() => {
              if (editing && hasValidationErrors) {
                Alert.alert(
                  "Measurements Required",
                  "Enter all inputs before proceeding."
                );
                return;
              }
              setEditing(!editing);
            }}
          >
            <MaterialIcons
              name={editing ? "check" : "edit"}
              size={20}
              color="#000"
              style={{ marginRight: 1 }}
            />
            <Text style={styles.editButtonText}>
              {editing ? "Done" : "Edit"}
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[
              styles.button,
              styles.saveButton,
              editing && hasValidationErrors && { opacity: 0.5 },
            ]}
            disabled={editing && hasValidationErrors}
            onPress={() => {
              if (editing && hasValidationErrors) {
                Alert.alert(
                  "Validation Error",
                  "Fix input errors before saving."
                );
                return;
              }
              handleSave();
            }}
          >
            <MaterialIcons
              name="save"
              size={20}
              color="#fff" // White icon for save button
              style={{ marginRight: 6 }}
            />
            <Text style={styles.buttonText}>Save</Text>
          </TouchableOpacity>
        </View>
      </View>
    </SafeAreaView>
  );
};

export default DigitalTwinPreviewScreen;

// 🔧 Updated Styles
const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: "#ffffff",
    paddingTop: Platform.OS === "android" ? 40 : 60,
  },
  heading: {
    fontSize: 24,
    fontWeight: "600",
    textAlign: "center",
    marginBottom: 20,
    color: "#1f2937",
  },
  contentRow: {
    flexDirection: "row",
    paddingHorizontal: 16,
  },
  imageWrapper: {
    flex: 6, // 60%
    justifyContent: "center",
    alignItems: "center",
  },

  image: {
    height: height * 0.75,
    width: "100%",
    borderRadius: 12,
    backgroundColor: "#e2e8f0",
  },

  rightSection: {
    flex: 4,
    paddingLeft: 16,
    maxHeight: height * 0.75,
  },

  measurementAndButtonsContainer: {
    flex: 1,
    justifyContent: "space-between",
  },
  measurementSection: {
    paddingBottom: 10,
  },
  subheading: {
    fontSize: 18,
    fontWeight: "500",
    marginBottom: 14,
    color: "#1e293b",
  },
  measurementListContainer: {},
  measureItem: {
    marginBottom: 12,
    backgroundColor: "#f9fafb",
    paddingVertical: 10,
    paddingHorizontal: 12,
    borderRadius: 10,
    borderColor: "#e5e7eb",
    borderWidth: 1,
    shadowColor: "#000",
    shadowOpacity: 0.02,
    shadowRadius: 2,
    shadowOffset: { width: 0, height: 1 },
  },
  measureLabelNew: {
    fontSize: 14,
    fontWeight: "500",
    color: "#374151",
    marginBottom: 4,
    textAlign: "left",
  },
  measureValueWrapperNew: {
    width: "100%",
    alignItems: "flex-start",
  },
  mainContent: {
    flex: 1,
    justifyContent: "space-between",
  },
  measureTextNew: {
    fontSize: 16,
    color: "#111827",
    textAlign: "left",
    width: "100%",
  },
  bottomButtonContainer: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    paddingVertical: 12,
    gap: 12,
    borderTopWidth: 1,
    borderColor: "#e5e7eb",
    backgroundColor: "#ffffff",
  },
  scrollContent: {
    paddingBottom: 20,
  },

  inputNew: {
    fontSize: 16,
    color: "#111827",
    textAlign: "left",
    backgroundColor: "#ffffff",
    borderRadius: 8,
    borderWidth: 1,
    borderColor: "#d1d5db",
    width: "100%",
    minHeight: 25,
    paddingVertical: Platform.OS === "ios" ? 5 : 0,
    paddingHorizontal: 8,
  },
  buttonContainerNew: {
    height: 60,
    flexDirection: "row",
    justifyContent: "space-evenly",
    alignItems: "center",
    borderTopWidth: 1,
    borderTopColor: "#e5e7eb",
    backgroundColor: "#ffffff",
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 16,
    borderRadius: 8,
    elevation: 1,
  },
  buttonText: {
    color: "#ffffff",
    fontWeight: "600",
    fontSize: 16,
  },
  editButtonText: {
    color: "#000",
    fontWeight: "600",
    fontSize: 16,
  },
  editButton: {
    backgroundColor: "#fff",
    borderColor: "#000",
    borderWidth: 1,
    flexDirection: "row",
  },
  saveButton: {
    backgroundColor: "#58616e",
    flexDirection: "row",
  },
});
