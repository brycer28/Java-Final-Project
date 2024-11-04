package Logic;

public enum CardType {

    ACEHEART("ah.jpg"), ACEDIAMOND("ad.jpg"), ACESPADE("as.jpg"), ACECLUB("ac.jpg"),
    TWOHEART("2h.jpg"), TWODIAMOND("2d.jpg"), TWOSPADE("2s.jpg"), TWOCLUB("2c.jpg"),
    THREEHEART("3h.jpg"), THREEDIAMOND("3d.jpg"), THREESPADE("3s.jpg"), THREECLUB("3c.jpg"),
    FOURHEART("4h.jpg"), FOURDIAMOND("4d.jpg"), FOURSPADE("4s.jpg"), FOURCLUB("4c.jpg"),
    FIVEHEART("5h.jpg"), FIVEDIAMOND("5d.jpg"), FIVESPADE("5s.jpg"), FIVECLUB("5c.jpg"),
    SIXHEART("6h.jpg"), SIXDIAMOND("6d.jpg"), SIXSPADE("6s.jpg"), SIXCLUB("6c.jpg"),
    SEVENHEART("7h.jpg"), SEVENDIAMOND("7d.jpg"), SEVENSPADE("7s.jpg"), SEVENCLUB("7c.jpg"),
    EIGHTHEART("8c.jpg"), EIGHTDIAMOND("8d.jpg"), EIGHTSPADE("8s.jpg"), EIGHTCLUB("8c.jpg"),
    NINEHEART("9c.jpg"), NINEDIAMOND("9d.jpg"), NINESPADE("9s.jpg"), NINECLUB("9c.jpg"),
    TENHEART("10c.jpg"), TENDIAMOND("10d.jpg"), TENSPADE("10s.jpg"), TENCLUB("10c.jpg"),
    JACKHEART("jc.jpg"), JACKDIAMOND("jd.jpg"), JACKSPADE("js.jpg"), JACKCLUB("jc.jpg"),
    QUEENHEART("qc.jpg"), QUEENDIAMOND("qd.jpg"), QUEENSPADE("qs.jpg"), QUEENCLUB("qc.jpg"),
    KINGHEART("kc.jpg"), KINGDIAMOND("kd.jpg"), KINGSPADE("ks.jpg"), KINGCLUB("kc.jpg"),
    BACK("back.jpg"), JOKER("joker.jpg");

    public final String path;

    CardType(String path) {
        this.path = path;
    }
}
