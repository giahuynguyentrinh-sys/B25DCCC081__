import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DuLieuKhongHopLeException extends Exception {
    public DuLieuKhongHopLeException(String message) {
        super(message);
    }
}

abstract class SinhVien {
    protected static final long DON_GIA = 450000;
    protected String maSV;
    protected String maHe;
    protected int soTinChi;

    public SinhVien(String maHe, String maSV, int soTinChi) {
        this.maHe = maHe;
        this.maSV = maSV;
        this.soTinChi = soTinChi;
    }

    public abstract long hocPhi();

    public String getMaSV() { return maSV; }
    public String getMaHe() { return maHe; }
}

class SinhVienChinhQuy extends SinhVien {
    public SinhVienChinhQuy(String maSV, int soTinChi) {
        super("CQ", maSV, soTinChi);
    }

    @Override
    public long hocPhi() {
        return soTinChi * DON_GIA;
    }
}

class SinhVienChatLuongCao extends SinhVien {
    public SinhVienChatLuongCao(String maSV, int soTinChi) {
        super("CLC", maSV, soTinChi);
    }

    @Override
    public long hocPhi() {
        return soTinChi * DON_GIA * 3 / 2;
    }
}

class SinhVienLienThong extends SinhVien {
    public SinhVienLienThong(String maSV, int soTinChi) {
        super("LT", maSV, soTinChi);
    }

    @Override
    public long hocPhi() {
        return soTinChi * DON_GIA + 2000000;
    }
}

class QuanLyHocPhi {
    private final List<SinhVien> ds = new ArrayList<>();
    private final Set<String> daCo = new HashSet<>();

    // Them sinh vien tu mot dong du lieu; nem ngoai le neu khong hop le
    public void them(String he, String ma, String tinChi) throws DuLieuKhongHopLeException {
        if (!he.equals("CQ") && !he.equals("CLC") && !he.equals("LT")) {
            throw new DuLieuKhongHopLeException("Ma he khong hop le");
        }
        if (!tinChi.matches("[+-]?\\d+")) {
            throw new DuLieuKhongHopLeException("So tin chi khong phai so nguyen");
        }
        int tc;
        try {
            tc = Integer.parseInt(tinChi);
        } catch (NumberFormatException e) {
            throw new DuLieuKhongHopLeException("So tin chi ngoai doan [1, 30]");
        }
        if (tc < 1 || tc > 30) {
            throw new DuLieuKhongHopLeException("So tin chi ngoai doan [1, 30]");
        }
        if (daCo.contains(ma)) {
            throw new DuLieuKhongHopLeException("Trung ma sinh vien");
        }
        SinhVien sv;
        if (he.equals("CQ")) sv = new SinhVienChinhQuy(ma, tc);
        else if (he.equals("CLC")) sv = new SinhVienChatLuongCao(ma, tc);
        else sv = new SinhVienLienThong(ma, tc);
        ds.add(sv);
        daCo.add(ma);
    }

    public void sapXep() {
        ds.sort((a, b) -> {
            int c = Long.compare(b.hocPhi(), a.hocPhi());
            if (c != 0) return c;
            return a.getMaSV().compareTo(b.getMaSV());
        });
    }

    public List<SinhVien> getDs() {
        return ds;
    }

    public long tongHocPhi() {
        long t = 0;
        for (SinhVien sv : ds) t += sv.hocPhi();
        return t;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        int n = Integer.parseInt(line.trim());
        QuanLyHocPhi ql = new QuanLyHocPhi();
        StringBuilder out = new StringBuilder();
        int dem = 0;
        while (dem < n && (line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            dem++;
            String[] p = line.trim().split("\\s+");
            String he = p.length > 0 ? p[0] : "";
            String ma = p.length > 1 ? p[1] : "";
            String tc = p.length > 2 ? p[2] : "";
            try {
                ql.them(he, ma, tc);
            } catch (DuLieuKhongHopLeException e) {
                out.append("INVALID ").append(ma).append("\n");
            }
        }
        ql.sapXep();
        for (SinhVien sv : ql.getDs()) {
            out.append(sv.getMaSV()).append(" ")
               .append(sv.getMaHe()).append(" ")
               .append(sv.hocPhi()).append("\n");
        }
        out.append("TONG ").append(ql.tongHocPhi()).append("\n");
        System.out.print(out);
    }
}
