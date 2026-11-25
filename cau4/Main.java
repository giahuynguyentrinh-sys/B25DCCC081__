import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

interface MienGiam {
    long tinhGiam(long hocPhi);
}

class ConChinhSach implements MienGiam {
    @Override
    public long tinhGiam(long hocPhi) {
        return hocPhi * 50 / 100;
    }
}

class HoNgheo implements MienGiam {
    @Override
    public long tinhGiam(long hocPhi) {
        return hocPhi * 70 / 100;
    }
}

class HocBong implements MienGiam {
    @Override
    public long tinhGiam(long hocPhi) {
        return Math.min(3000000L, hocPhi);
    }
}

abstract class SinhVien {
    protected static final long DON_GIA = 450000;
    protected String maSV;
    protected String maHe;
    protected int soTinChi;
    protected MienGiam mienGiam; // co the null

    public SinhVien(String maHe, String maSV, int soTinChi) {
        this.maHe = maHe;
        this.maSV = maSV;
        this.soTinChi = soTinChi;
    }

    public abstract long hocPhi();

    public void setMienGiam(MienGiam mg) { this.mienGiam = mg; }

    public long soTienGiam() {
        return mienGiam == null ? 0 : mienGiam.tinhGiam(hocPhi());
    }

    public long phaiNop() {
        return hocPhi() - soTienGiam();
    }

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

public class Main {
    static MienGiam taoChinhSach(String ma) {
        switch (ma) {
            case "CS": return new ConChinhSach();
            case "HN": return new HoNgheo();
            case "HB": return new HocBong();
            default: return null;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<SinhVien> ds = new ArrayList<>();
        Map<String, SinhVien> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String he = sc.next();
            String ma = sc.next();
            int tc = sc.nextInt();
            SinhVien sv;
            if (he.equals("CQ")) sv = new SinhVienChinhQuy(ma, tc);
            else if (he.equals("CLC")) sv = new SinhVienChatLuongCao(ma, tc);
            else sv = new SinhVienLienThong(ma, tc);
            ds.add(sv);
            map.put(ma, sv);
        }
        StringBuilder out = new StringBuilder();
        int m = sc.hasNextInt() ? sc.nextInt() : 0;
        for (int i = 0; i < m; i++) {
            String ma = sc.next();
            String cs = sc.next();
            SinhVien sv = map.get(ma);
            if (sv == null) {
                out.append("NOT FOUND ").append(ma).append("\n");
            } else {
                sv.setMienGiam(taoChinhSach(cs));
            }
        }
        for (SinhVien sv : ds) {
            out.append(sv.getMaSV()).append(" ")
               .append(sv.getMaHe()).append(" ")
               .append(sv.hocPhi()).append(" ")
               .append(sv.soTienGiam()).append(" ")
               .append(sv.phaiNop()).append("\n");
        }
        String[] cacHe = {"CQ", "CLC", "LT"};
        for (String he : cacHe) {
            int dem = 0;
            long tong = 0;
            for (SinhVien sv : ds) {
                if (sv.getMaHe().equals(he)) {
                    dem++;
                    tong += sv.phaiNop();
                }
            }
            if (dem > 0) {
                out.append(he).append(" ").append(dem).append(" ").append(tong).append("\n");
            }
        }
        System.out.print(out);
    }
}
