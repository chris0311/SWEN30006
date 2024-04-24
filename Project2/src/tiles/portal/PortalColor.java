package src.tiles.portal;

public enum PortalColor {
    WHITE,
    YELLOW,
    DARKGOLD,
    DARKGRAY;

    public String getImageName() {
        switch (this) {
            case WHITE -> {
                return "i_portalWhiteTile_resize.png";
            }
            case YELLOW -> {
                return "j_portalYellowTile_resize.png";
            }
            case DARKGOLD -> {
                return "k_portalDarkGoldTile_resize.png";
            }
            case DARKGRAY -> {
                return "l_portalDarkGrayTile_resize.png";
            }
            default -> {
                assert false;
            }
        }
        return null;
    }

    public char getChar() {
        switch (this) {
            case WHITE -> {
                return 'i';
            }
            case YELLOW -> {
                return 'j';
            }
            case DARKGOLD -> {
                return 'k';
            }
            case DARKGRAY -> {
                return 'l';
            }
            default -> {
                assert false;
            }
        }
        return ' ';
    }
}
