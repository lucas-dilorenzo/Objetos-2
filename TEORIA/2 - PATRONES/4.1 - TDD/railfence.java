public class RailFenceCipher {

    public String encrypt(String text, int key) {
        char[][] rail = new char[key][text.length()];
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                rail[i][j] = '\n';
            }
        }

        boolean down = false;
        int row = 0;

        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1) {
                down = !down;
            }

            rail[row][i] = text.charAt(i);

            if (down) {
                row++;
            } else {
                row--;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] != '\n') {
                    result.append(rail[i][j]);
                }
            }
        }
        return result.toString();
    }

    public String decrypt(String text, int key) {
        char[][] rail = new char[key][text.length()];
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                rail[i][j] = '\n';
            }
        }

        boolean down = false;
        int row = 0;

        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1) {
                down = !down;
            }

            rail[row][i] = '*';

            if (down) {
                row++;
            } else {
                row--;
            }
        }

        int index = 0;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (rail[i][j] == '*' && index < text.length()) {
                    rail[i][j] = text.charAt(index++);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        row = 0;
        down = false;

        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1) {
                down = !down;
            }

            result.append(rail[row][i]);

            if (down) {
                row++;
            } else {
                row--;
            }
        }
        return result.toString();
    }
}
