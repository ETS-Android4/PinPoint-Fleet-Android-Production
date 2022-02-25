package json_parsing;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceListData implements Parcelable {

	String accountID,deviceDescription, deviceID, displayName,deviceGroupName;

	public DeviceListData(String devId,String displayName) {

		this.deviceID = devId;
		this.displayName = displayName;
	}
	
	public DeviceListData(String account_id,String devDesc,
			String devId,String displayName,String deviceGroupName) {

		this.accountID = account_id;
		this.deviceDescription = devDesc;
		this.deviceID = devId;
		this.displayName = displayName;
		this.deviceGroupName = deviceGroupName;
	}

	public String getDeviceDescription() {
		return deviceDescription;
	}

	public void setDeviceDescription(String deviceDescription) {
		this.deviceDescription = deviceDescription;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getDeviceGroupName() {
		return deviceGroupName;
	}

	public void setDeviceGroupName(String deviceGroupName) {
		this.deviceGroupName = deviceGroupName;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(deviceDescription);
		dest.writeString(deviceID);
		dest.writeString(accountID);
		dest.writeString(displayName);
		dest.writeString(deviceGroupName);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public DeviceListData createFromParcel(Parcel in) {
			return new DeviceListData(in);
		}

		public DeviceListData[] newArray(int size) {
			return new DeviceListData[size];
		}
	};
	
	public DeviceListData(Parcel in) {
		deviceDescription = in.readString();
		deviceID = in.readString();
		accountID = in.readString();
		displayName = in.readString();
		deviceGroupName = in.readString();;
	}
}
