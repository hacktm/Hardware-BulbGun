using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using BluetoothClientWP8.Resources;
using Windows.Networking.Sockets;
using Windows.Networking.Proximity;
using System.Diagnostics;
using Windows.Storage.Streams;
using System.Threading.Tasks;
using BluetoothConnectionManager;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace BluetoothClientWP8
{
    public partial class MainPage : PhoneApplicationPage
    {
        private ConnectionManager connectionManager;
        private int status;
        private StateManager stateManager;

        // Constructor
        public MainPage()
        {
            InitializeComponent();
            connectionManager = new ConnectionManager();
            connectionManager.MessageReceived += connectionManager_MessageReceived;
            stateManager = new StateManager();
            status = 1;
        }

        async void connectionManager_MessageReceived(string message)
        {
            Debug.WriteLine("Message received:" + message);
            //TODO Update the status from data received rom arduino
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            connectionManager.Initialize();
            stateManager.Initialize();
        }

        protected override void OnNavigatedFrom(NavigationEventArgs e)
        {
            connectionManager.Terminate();
        }

        private void ConnectAppToDeviceButton_Click_1(object sender, RoutedEventArgs e)
        {
            AppToDevice();
        }

        private async void AppToDevice()
        {
            ConnectAppToDeviceButton.Content = "Connecting...";
            PeerFinder.AlternateIdentities["Bluetooth:Paired"] = "";
            var pairedDevices = await PeerFinder.FindAllPeersAsync();

            if (pairedDevices.Count == 0)
            {
                Debug.WriteLine("No paired devices were found.");
            }
            else
            { 
                foreach (var pairedDevice in pairedDevices)
                {
                    if (pairedDevice.DisplayName == DeviceName.Text)
                    {
                        connectionManager.Connect(pairedDevice.HostName);
                        ConnectAppToDeviceButton.Content = "Connected";
                        DeviceName.IsEnabled = false;
                        ConnectAppToDeviceButton.IsEnabled = false;
                        Status.Text = "You are connected to " + DeviceName.Text;
                        Status.Visibility = Visibility.Visible;
                        DisconnectAppToDeviceButton.Visibility = Visibility.Visible;
                        continue;
                    }
                }
            }
        }

        private async void Button_Click(object sender, RoutedEventArgs e)
        {
            ImageBrush on = new ImageBrush();
            on.ImageSource = new BitmapImage(new Uri("/Assets/Buttons/btn_on.png", UriKind.Relative));
            ImageBrush off = new ImageBrush();
            off.ImageSource = new BitmapImage(new Uri("/Assets/Buttons/btn_off.png", UriKind.Relative));

            if (status == 0)
            {
                string command = "1";
                await connectionManager.SendCommand(command);
                btn_switch.Background = on;
                status = 1;
            }

            else
            {
                string command = "0";
                await connectionManager.SendCommand(command);
                btn_switch.Background = off;
                status = 0;
            }
        }

        private void DisconnectAppToDeviceButton_Click_1(object sender, RoutedEventArgs e)
        {
            connectionManager.Terminate();
            ConnectAppToDeviceButton.Content = "Connect";
            DeviceName.IsEnabled = true;
            ConnectAppToDeviceButton.IsEnabled = true;
            Status.Visibility = Visibility.Collapsed;
            DisconnectAppToDeviceButton.Visibility = Visibility.Collapsed;
            DeviceName.Visibility = Visibility.Visible;
        }
    }
}