
package com.jakfarshodiq.jakfar.antriansimapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class RequestKodeAntrian {

    private String status;
    @SerializedName("id_antrian")
    private String idAntrian;
    @SerializedName("jenis_sim")
    private String jenisSim;
    @SerializedName("id_penduduk")
    private String idPenduduk;
    @SerializedName("nama_penduduk")
    private String namaPenduduk;
    @SerializedName("no_antrian")
    private String noAntrian;
    @SerializedName("status_antrian")
    private String statusAntrian;
    @SerializedName("no_antrian_berjalan")
    private String noAntrianBerjalan;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdAntrian() {
        return idAntrian;
    }

    public void setIdAntrian(String idAntrian) {
        this.idAntrian = idAntrian;
    }

    public String getJenisSim() {
        return jenisSim;
    }

    public void setJenisSim(String jenisSim) {
        this.jenisSim = jenisSim;
    }

    public String getIdPenduduk() {
        return idPenduduk;
    }

    public void setIdPenduduk(String idPenduduk) {
        this.idPenduduk = idPenduduk;
    }

    public String getNamaPenduduk() {
        return namaPenduduk;
    }

    public void setNamaPenduduk(String namaPenduduk) {
        this.namaPenduduk = namaPenduduk;
    }

    public String getNoAntrian() {
        return noAntrian;
    }

    public void setNoAntrian(String noAntrian) {
        this.noAntrian = noAntrian;
    }

    public String getStatusAntrian() {
        return statusAntrian;
    }

    public void setStatusAntrian(String statusAntrian) {
        this.statusAntrian = statusAntrian;
    }

    public String getNoAntrianBerjalan() {
        return noAntrianBerjalan;
    }

    public void setNoAntrianBerjalan(String noAntrianBerjalan) {
        this.noAntrianBerjalan = noAntrianBerjalan;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}