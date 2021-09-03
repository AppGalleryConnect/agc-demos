/*
        Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
using Android.App;
using Android.OS;
using Android.Support.V7.App;
using Android.Runtime;
using Android.Widget;
using Huawei.Agconnect.Auth;
using System.Threading.Tasks;
using Android.Util;
using System;
using Android.Views;
using Huawei.Agconnect.Cloud.Database;
using Huawei.Agconnect.Cloud.Database.Exceptions;
using XamarinHmsCloudDBDemo.Model;
using System.Collections.Generic;
using XamarinHmsCloudDBDemo.Helpers;
using Android.Content;
using XamarinHmsCloudDBDemo.Utils;

namespace XamarinHmsCloudDBDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity, View.IOnClickListener, AGConnectCloudDB.IOnDataEncryptionKeyChangeListener, IOnSnapshotListener
    {
        public const string Tag = "MainActivity";

        private AGConnectCloudDB mCloudDB;

        private CloudDBZone mCloudDBZone;

        private CloudDBZoneConfig mConfig;

        private ListenerHandler mRegister;


        BookInfo bookInfoTest = new BookInfo()
        {
            Author = "John Doe",
            BookName = "test name",
            Price = 150,
            Publisher = "ExamplePublisher"
        };

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.activity_main);

            Button CreateObjectTypeButton = (Button)FindViewById(Resource.Id.CreateObjectTypeButton);
            Button OpenCloudDBZone2Button = (Button)FindViewById(Resource.Id.OpenCloudDBZone2Button);
            Button CloudDBZoneConfigsButton = (Button)FindViewById(Resource.Id.CloudDBZoneConfigsButton);
            Button CloudDBZoneConfigButton = (Button)FindViewById(Resource.Id.CloudDBZoneConfigButton);
            Button CloseCloudDBButton = (Button)FindViewById(Resource.Id.CloseCloudDBButton);
            Button DeleteCloudDBZoneButton = (Button)FindViewById(Resource.Id.DeleteCloudDBZoneButton);
            Button EnableNetworkButton = (Button)FindViewById(Resource.Id.EnableNetworkButton);
            Button DisableNetworkButton = (Button)FindViewById(Resource.Id.DisableNetworkButton);
            Button UpdateDataEncryptionKeyButton = (Button)FindViewById(Resource.Id.UpdateDataEncryptionKeyButton);
            Button SetUserKeyButton = (Button)FindViewById(Resource.Id.SetUserKeyButton);
            Button ExecuteUpsertButton = (Button)FindViewById(Resource.Id.ExecuteUpsertButton);
            Button ExecuteDeleteButton = (Button)FindViewById(Resource.Id.ExecuteDeleteButton);
            Button ExecuteQueryButton = (Button)FindViewById(Resource.Id.ExecuteQueryButton);
            Button ExecuteAverageQueryButton = (Button)FindViewById(Resource.Id.ExecuteAverageQueryButton);
            Button ExecuteSumQueryButton = (Button)FindViewById(Resource.Id.ExecuteSumQueryButton);
            Button ExecuteMaximumQueryButton = (Button)FindViewById(Resource.Id.ExecuteMaximumQueryButton);
            Button ExecuteMinimalQueryButton = (Button)FindViewById(Resource.Id.ExecuteMinimalQueryButton);
            Button ExecuteCountQueryButton = (Button)FindViewById(Resource.Id.ExecuteCountQueryButton);
            Button ExecuteQueryUnsyncedButton = (Button)FindViewById(Resource.Id.ExecuteQueryUnsyncedButton);
            Button RunTransactionButton = (Button)FindViewById(Resource.Id.RunTransactionButton);
            Button UnSubscribeSnapshotButton = (Button)FindViewById(Resource.Id.UnSubscribeSnapshotButton);
            Button SubscribeSnapshotButton = (Button)FindViewById(Resource.Id.SubscribeSnapshotButton);

            
            CreateObjectTypeButton.SetOnClickListener(this);
            OpenCloudDBZone2Button.SetOnClickListener(this);
            CloudDBZoneConfigsButton.SetOnClickListener(this);
            CloudDBZoneConfigButton.SetOnClickListener(this);
            CloseCloudDBButton.SetOnClickListener(this);
            DeleteCloudDBZoneButton.SetOnClickListener(this);
            EnableNetworkButton.SetOnClickListener(this);
            DisableNetworkButton.SetOnClickListener(this);
            UpdateDataEncryptionKeyButton.SetOnClickListener(this);
            SetUserKeyButton.SetOnClickListener(this);
            ExecuteUpsertButton.SetOnClickListener(this);
            ExecuteDeleteButton.SetOnClickListener(this);
            ExecuteQueryButton.SetOnClickListener(this);
            ExecuteAverageQueryButton.SetOnClickListener(this);
            ExecuteSumQueryButton.SetOnClickListener(this);
            ExecuteMaximumQueryButton.SetOnClickListener(this);
            ExecuteMinimalQueryButton.SetOnClickListener(this);
            ExecuteCountQueryButton.SetOnClickListener(this);
            ExecuteQueryUnsyncedButton.SetOnClickListener(this);
            RunTransactionButton.SetOnClickListener(this);
            UnSubscribeSnapshotButton.SetOnClickListener(this);
            SubscribeSnapshotButton.SetOnClickListener(this);

            InitAGConnectCloudDB(this);
            mCloudDB = AGConnectCloudDB.Instance;
            mCloudDB.AddDataEncryptionKeyListener(this);
            Login();
        }

        public static void InitAGConnectCloudDB(Context context)
        {
            AGConnectCloudDB.Initialize(context);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        /// <summary>
        /// The Anonymous account authentication method
        /// for the sample application to use permissions of the authentication user.
        /// </summary>
        public async void Login()
        {
            AGConnectAuth auth = AGConnectAuth.Instance;
            Task<ISignInResult> signInTask = auth.SignInAnonymouslyAsync();
            try
            {
                await signInTask;

                if (signInTask.Result != null)
                {
                    // Sign in success.
                    Toast.MakeText(this, "Sign In success", ToastLength.Long).Show();
                }
                else
                {
                    // Sign in fail.
                    Toast.MakeText(this, "Sign In failed: " + signInTask.Exception.Message, ToastLength.Long).Show();

                }

            }
            catch (Exception e)
            {
                Log.Error(Tag, "sign in for agc failed: " + e.Message);
            }
        }

        public async void OnClick(View v)
        {
            switch (v.Id)
            {
                case Resource.Id.CreateObjectTypeButton:
                    CreateObjectType();
                    break;
                case Resource.Id.OpenCloudDBZone2Button:
                    OpenCloudDBZoneV2();
                    break;
                case Resource.Id.CloudDBZoneConfigsButton:
                    ObtainCloudDBZoneConfigs();
                    break;
                case Resource.Id.CloudDBZoneConfigButton:
                    ObtainCloudDBZoneConfig();
                    break;
                case Resource.Id.CloseCloudDBButton:
                    CloseCloudDB();
                    break;
                case Resource.Id.DeleteCloudDBZoneButton:
                    DeleteCloudDBZone();
                    break;
                case Resource.Id.EnableNetworkButton:
                    EnableNetwork();
                    break;
                case Resource.Id.DisableNetworkButton:
                    DisableNetwork();
                    break;
                case Resource.Id.UpdateDataEncryptionKeyButton:
                    UpdateDataEncryptionKey();
                    break;
                case Resource.Id.SetUserKeyButton:
                    SetUserKey();
                    break;
                case Resource.Id.ExecuteUpsertButton:
                    ExecuteUpsert();
                    break;
                case Resource.Id.ExecuteDeleteButton:
                    ExecuteDelete();
                    break;
                case Resource.Id.ExecuteQueryButton:
                    ExecuteQuery();
                    break;
                case Resource.Id.ExecuteAverageQueryButton:
                    ExecuteAverageQuery();
                    break;
                case Resource.Id.ExecuteSumQueryButton:
                    ExecuteSumQuery();
                    break;
                case Resource.Id.ExecuteMaximumQueryButton:
                    ExecuteMaximumQuery();
                    break;
                case Resource.Id.ExecuteMinimalQueryButton:
                    ExecuteMinimalQuery();
                    break;
                case Resource.Id.ExecuteCountQueryButton:
                    ExecuteCountQuery();
                    break;
                case Resource.Id.ExecuteQueryUnsyncedButton:
                    ExecuteQueryUnsynced();
                    break;
                case Resource.Id.RunTransactionButton:
                    RunTransaction();
                    break;
                case Resource.Id.UnSubscribeSnapshotButton:
                    UnSubscribeSnapshot();
                    break;
                case Resource.Id.SubscribeSnapshotButton:
                    SubscribeSnapshot();
                    break;
                default:
                    break;
            }
        }

        /// <summary>
        /// Shows given text in alert dialog.
        /// </summary>
        /// <param name="text">text</param>
        public void ShowResultPopup(string text)
        {
            Android.Support.V7.App.AlertDialog.Builder alertDialog = new Android.Support.V7.App.AlertDialog.Builder(this);
            alertDialog.SetTitle("Result");
            alertDialog.SetMessage(text);

            alertDialog.SetPositiveButton("OK", delegate
            {
                alertDialog.Dispose();
            });

            alertDialog.Show();
        }

        /// <summary>
        /// Call AGConnectCloudDB.CreateObjectType to init schema.
        /// </summary>
        public void CreateObjectType()
        {
            try
            {
                var objectTypeInfoHelper = Java.Lang.Class.ForName("com.company.project.ObjectTypeInfoHelper");
                var javaMethod = objectTypeInfoHelper.GetMethod("getObjectTypeInfo");
                ObjectTypeInfo objectTypeInfo = (ObjectTypeInfo)javaMethod.Invoke(objectTypeInfoHelper);
                mCloudDB.CreateObjectType(objectTypeInfo);
                ShowResultPopup(
                    "ObjectType created successfully"
                    + "\n" +
                    "ObjectTypeVersion: " + objectTypeInfo.ObjectTypeVersion
                    + "\n" +
                    "FormatVersion:" + objectTypeInfo.FormatVersion
                    );

            }
            catch (Exception e)
            {
                ShowResultPopup("CreateObjectType failed: " + e.Message);
            }
        }

        /// <summary>
        /// Asynchronously creates or opens an object of a Cloud DB zone.
        /// </summary>
        public async void OpenCloudDBZoneV2()
        {
            // Create CloudDBZoneConfig object.
            mConfig = new CloudDBZoneConfig("QuickStartDemo",
                    CloudDBZoneConfig.CloudDBZoneSyncProperty.ClouddbzoneCloudCache,
                    CloudDBZoneConfig.CloudDBZoneAccessProperty.ClouddbzonePublic);
            mConfig.PersistenceEnabled = true;

            // Call OpenCloudDBZone2Async method to open CloudDBZone.
            Task<CloudDBZone> openDBZoneTask = mCloudDB.OpenCloudDBZone2Async(mConfig, true);
            try
            {
                await openDBZoneTask;

                if (openDBZoneTask.Result != null)
                {
                    // Open clouddbzone success.

                    mCloudDBZone = openDBZoneTask.Result;
                    ShowResultPopup("OpenCloudDBZoneV2 executed successfully");

                    // Add subscription after opening cloudDBZone success.
                    SubscribeSnapshot();
                }
            }
            catch (Exception e)
            {
                // open clouddbzone failed.
                ShowResultPopup("open clouddbzone failed for " + e.Message);
            }

        }


        public void ObtainCloudDBZoneConfigs()
        {
            ShowResultPopup(
                "CloudDBZoneConfigs contains " + mCloudDB.CloudDBZoneConfigs.Count + " configs"
                );
        }

        public void ObtainCloudDBZoneConfig()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            var cloudDBZoneConfig =  mCloudDBZone.CloudDBZoneConfig;
            ShowResultPopup(
                "CloudDBZoneConfig"
                + "\n" +
                "Capacity: " + cloudDBZoneConfig.Capacity.ToString()
                + "\n" +
                "CloudDBZoneName:" + cloudDBZoneConfig.CloudDBZoneName.ToString()
                + "\n" +
                "PersistenceEnabled:" + cloudDBZoneConfig.PersistenceEnabled.ToString()
                  + "\n" +
                "IsEncrypted:" + cloudDBZoneConfig.IsEncrypted.ToString()
                 + "\n" +
                "AccessProperty:" + cloudDBZoneConfig.AccessProperty.ToString()
                + "\n" +
                "SyncProperty:" + cloudDBZoneConfig.SyncProperty.ToString()
                );
        }

        public void CloseCloudDB()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            try
            {
                UnSubscribeSnapshot();
                mCloudDB.CloseCloudDBZone(mCloudDBZone);
                ShowResultPopup("CloseCloudDBZone executed successfully");
            }
            catch (Exception e)
            {
                ShowResultPopup("CloseCloudDBZone failed: " + e.Message);
            }
        }

        public void DeleteCloudDBZone()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            try
            {
                mCloudDB.DeleteCloudDBZone(mConfig.CloudDBZoneName);
                ShowResultPopup("DeleteCloudDBZone executed successfully");
            }
            catch (Exception e)
            {
                ShowResultPopup("DeleteCloudDBZone failed: " + e.Message);
            }
        }

        public void EnableNetwork()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            try
            {
                mCloudDB.EnableNetwork("QuickStartDemo");
                ShowResultPopup("EnableNetwork executed successfully");
            }
            catch(Exception e)
            {
                ShowResultPopup("EnableNetwork failed: " + e.Message);
            }
        }

        public void DisableNetwork()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            try
            {
                mCloudDB.DisableNetwork("QuickStartDemo");
                ShowResultPopup("DisableNetwork executed successfully");
            }
            catch (Exception e)
            {
                ShowResultPopup("DisableNetwork failed: " + e.Message);
            }
        }

        public async void UpdateDataEncryptionKey()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            Task<bool> updateDataKeyTask = mCloudDB.UpdateDataEncryptionKeyAsync();
            try
            {
                await updateDataKeyTask;

                bool isSuccess = updateDataKeyTask.Result;
                if (isSuccess)
                {
                    // update data key success.
                    ShowResultPopup("UpdateDataEncryptionKeyAsync executed successfully");
                }
                else
                {
                    // update data key fail.
                    ShowResultPopup("UpdateDataEncryptionKeyAsync failed");
                }
            }
            catch (Exception e)
            {
                // Exception
                ShowResultPopup("UpdateDataEncryptionKeyAsync failed: "+ e.Message);
                ShowResultPopup("Please tap SetUserKey button first!");
            }
        }

        public async void SetUserKey()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            Task<bool> setUserKeyTask = mCloudDB.SetUserKeyAsync("123456789", null);
            try
            {
                await setUserKeyTask;

                bool isSuccess = setUserKeyTask.Result;
                if (isSuccess)
                {
                    //set user key success.
                    ShowResultPopup("SetUserKey executed successfully");
                }
                else
                {
                    //set user key fail.
                    ShowResultPopup("SetUserKey failed.");
                }
            }
            catch (Exception e)
            {
                // Exception
                ShowResultPopup("SetUserKey failed: "+ e.Message);
            }
        }

        public async void ExecuteUpsert()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneObject bookInfoObject = (CloudDBZoneObject)ObjectTypeHelper.ConvertCSharpTypeToJavaType(bookInfoTest);
            Task<int> upsertTask = mCloudDBZone.ExecuteUpsertAsync(bookInfoObject);
            try
            {
                await upsertTask;
                int cloudDBZoneResult = upsertTask.Result;
                ShowResultPopup(cloudDBZoneResult + " records upserted");
            }
            catch (Exception e)
            {
                ShowResultPopup("Insert book info failed" + e.Message);
            }
        }

        public async void ExecuteDelete()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            IList<CloudDBZoneObject> bookInfoList = new List<CloudDBZoneObject>();
            CloudDBZoneObject bookInfoObject = (CloudDBZoneObject)Helpers.ObjectTypeHelper.ConvertCSharpTypeToJavaType(bookInfoTest);
            bookInfoList.Add(bookInfoObject);
            Task<int> deleteTask = mCloudDBZone.ExecuteDeleteAsync(bookInfoList);
            try
            {
                await deleteTask;
                ShowResultPopup("ExecuteDelete successfully. result: " + deleteTask.Result);
            }
            catch (Exception e)
            {
                ShowResultPopup("Delete book info failed" + e.Message);
            }
        }

        public async void ExecuteQuery()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            Task<CloudDBZoneSnapshot> queryTask = mCloudDBZone.ExecuteQueryAsync(
                CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo")),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.PolicyQueryFromCloudOnly);
            try
            {
                await queryTask;
                if (queryTask.Result != null)
                {
                    CloudDBZoneSnapshot snapshot = queryTask.Result;
                    ProcessQueryResult(snapshot);
                }
            }
            catch (Exception e)
            {
                ShowResultPopup("QueryAllBooks failed: " + e.Message);
            }
        }

        public async void ExecuteAverageQuery()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneQuery query = CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo"));
            Task<double> averageQueryTask = mCloudDBZone.ExecuteAverageQueryAsync(query, BookEditFields.Price,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.PolicyQueryFromCloudOnly);
            try
            {
                await averageQueryTask;
                ShowResultPopup("Average price is " + averageQueryTask.Result);
                
            }
            catch (Exception e)
            {
                ShowResultPopup("Average query is failed: " + e.Message);
            }
        }

        public async void ExecuteSumQuery()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneQuery query = CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo"));
            Task<Java.Lang.Number> averageQueryTask = mCloudDBZone.ExecuteSumQueryAsync(query, BookEditFields.Price,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.PolicyQueryFromCloudOnly);
            try
            {
                await averageQueryTask;
                if (averageQueryTask.Result != null)
                {
                    ShowResultPopup("Sum of prices is " + averageQueryTask.Result.ToString());
                }
            }
            catch (Exception e)
            {
                ShowResultPopup("Sum query is failed:" + e.Message);
            }
        }

        public async void ExecuteMaximumQuery()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneQuery query = CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo"));
            Task<Java.Lang.Number> maximumQueryTask = mCloudDBZone.ExecuteMaximumQueryAsync(query, BookEditFields.Price,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.PolicyQueryFromCloudOnly);
            try
            {
                await maximumQueryTask;
                if (maximumQueryTask.Result != null)
                {
                    ShowResultPopup("Maximum price is " + maximumQueryTask.Result.ToString());
                }
            }
            catch (Exception e)
            {
                ShowResultPopup("Maximum query is failed:" + e.Message);
            }
        }

        public async void ExecuteMinimalQuery()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneQuery query = CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo"));
            Task<Java.Lang.Number> minimalQueryTask = mCloudDBZone.ExecuteMinimalQueryAsync(query, BookEditFields.Price,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.PolicyQueryFromCloudOnly);
            try
            {
                await minimalQueryTask;
                if (minimalQueryTask.Result != null)
                {
                    ShowResultPopup("Minimum price is " + minimalQueryTask.Result.ToString());
                }
            }
            catch (Exception e)
            {
                ShowResultPopup("Minimum query is failed:" + e.Message);
            }
        }

        public async void ExecuteCountQuery()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneQuery query = CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo"));
            Task<long> countQueryTask = mCloudDBZone.ExecuteCountQueryAsync(query, BookEditFields.BookID,
                            CloudDBZoneQuery.CloudDBZoneQueryPolicy.PolicyQueryFromCloudOnly);
            try
            {
                await countQueryTask;
                ShowResultPopup("Count is " + countQueryTask.Result.ToString());
            }
            catch (Exception e)
            {
                ShowResultPopup("Count query is failed:" + e.Message);
            }
        }

        public async void ExecuteQueryUnsynced()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneQuery query = CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo"));
            Task<CloudDBZoneSnapshot> unsyncedQueryTask = mCloudDBZone.ExecuteQueryUnsyncedAsync(query);
            try
            {
                await unsyncedQueryTask;
                if (unsyncedQueryTask.Result != null)
                {
                    CloudDBZoneSnapshot snapshot = unsyncedQueryTask.Result;
                    ProcessQueryResult(snapshot);
                }
            }
            catch (Exception e)
            {
                ShowResultPopup("Unsynced query is failed:" + e.Message);
            }
        }

        /// <summary>
        /// Delete overdue books.
        /// </summary>
        public async void RunTransaction()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }

            CloudDBZoneQuery query = CloudDBZoneQuery.Where(Java.Lang.Class.ForName("com.company.project.BookInfo")).
                LessThan(BookEditFields.PublishTime, DateUtils.ParseDate("2020-12-04"));

            Transaction.IFunction function = new TransactionFunction((transaction) => {

                try
                {
                    System.Collections.IList bookInfos = transaction.ExecuteQuery(query);
                    IList<CloudDBZoneObject> bookList = new List<CloudDBZoneObject>();
                    foreach (Java.Lang.Object item in bookInfos)
                    {
                        bookList.Add((CloudDBZoneObject)item);
                    }
                    transaction.ExecuteDelete(bookList);
                }
                catch (AGConnectCloudDBException e)
                {
                    ShowResultPopup("Transaction.IFunction exception: " + e.ErrMsg);
                }
            });

            try
            {
                await mCloudDBZone.RunTransactionAsync(function);
                ShowResultPopup("RunTransaction executed successfully");
            }
            catch (Exception e)
            {
                ShowResultPopup("RunTransaction failed: " + e.Message);
            }
        }

        public void UnSubscribeSnapshot()
        {
            if (mRegister == null)
            {
                ShowResultPopup("Please tap SubscribeSnapshot button first!");
                return;
            }

            try
            {
                mRegister.Remove();
                ShowResultPopup("UnSubscribeSnapshot executed successfully.");
            }
            catch (Exception e)
            {
                ShowResultPopup("UnSubscribeSnapshot: " + e.Message);
            }
        }

        public void SubscribeSnapshot()
        {
            if (mCloudDBZone == null)
            {
                ShowResultPopup("Please open CloudDBZone first!");
                return;
            }
            try
            {
                CloudDBZoneQuery snapshotQuery = CloudDBZoneQuery
                                                .Where(Java.Lang.Class.ForName("com.company.project.BookInfo"))
                                                .EqualTo(BookEditFields.ShadowFlag, true);
                mRegister = mCloudDBZone.SubscribeSnapshot(snapshotQuery,
                    CloudDBZoneQuery.CloudDBZoneQueryPolicy.PolicyQueryFromCloudOnly, this);
                ShowResultPopup("SubscribeSnapshot executed successfully.");
            }
            catch (Exception e)
            {
                ShowResultPopup("subscribeSnapshot failed: " + e.Message);
            }
        }

        public bool NeedFetchDataEncryptionKey()
        {
            return true;
        }

        public void OnSnapshot(CloudDBZoneSnapshot cloudDBZoneSnapshot, AGConnectCloudDBException e)
        {
            if (e != null)
            {
                Log.Error(Tag, "OnSnapshot: " + e.Message);
                return;
            }
            CloudDBZoneObjectList snapshotObjects = cloudDBZoneSnapshot.SnapshotObjects;

            IList<BookInfo> bookInfoList = new List<BookInfo>();
            try
            {
                if (snapshotObjects != null)
                {
                    while (snapshotObjects.HasNext)
                    {
                        BookInfo bookInfo = Helpers.ObjectTypeHelper.ConvertJavaTypeToCSharpType(snapshotObjects.Next());
                        bookInfoList.Add(bookInfo);
                    }
                }
            }
            catch (AGConnectCloudDBException snapshotException)
            {
                Log.Error(Tag, "OnSnapshot:(getObject) " + snapshotException.Message);
            }
            finally
            {
                cloudDBZoneSnapshot.Release();
            }
        }

        private void ProcessQueryResult(CloudDBZoneSnapshot snapshot)
        {
            CloudDBZoneObjectList bookInfoCursor = snapshot.SnapshotObjects;
            string bookNames = "";
            try
            {
                while (bookInfoCursor.HasNext)
                {
                    BookInfo bookInfo = Helpers.ObjectTypeHelper.ConvertJavaTypeToCSharpType(bookInfoCursor.Next());
                    bookNames += bookInfo.BookName + ", ";

                    if (bookInfo.Author.Equals(bookInfoTest.Author))
                    {
                        bookInfoTest.Id = bookInfo.Id;
                    }
                }
            }
            catch (Exception e)
            {
                Log.Equals(Tag, "processQueryResult: " + e.Message);
            }
            finally
            {
                snapshot.Release();
            }
            if (bookNames.Equals(""))
            {
                ShowResultPopup("There is no record.");
            }
            else
            {
                ShowResultPopup(bookNames);
            }
        }

        /// <summary>
        /// TransactionFunction class that implements Transaction.IFunction
        /// to make transaction operations.
        /// </summary>
        public class TransactionFunction : Java.Lang.Object, Transaction.IFunction
        {
            private Action<Transaction> transactionAction;

            public TransactionFunction(Action<Transaction> action)
            {
                this.transactionAction = action;
            }

            public bool Apply(Transaction transaction)
            {
                transactionAction(transaction);
                return true;
            }
        }
    }
}