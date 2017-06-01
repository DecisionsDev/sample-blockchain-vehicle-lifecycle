#!/bin/bash
set -ev

# Get the current directory.
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Get the full path to this script.
SOURCE="${DIR}/composer.sh"

# Create a work directory for extracting files into.
WORKDIR="$(pwd)/composer-data"
rm -rf "${WORKDIR}" && mkdir -p "${WORKDIR}"
cd "${WORKDIR}"

# Find the PAYLOAD: marker in this script.
PAYLOAD_LINE=$(grep -a -n '^PAYLOAD:$' "${SOURCE}" | cut -d ':' -f 1)
echo PAYLOAD_LINE=${PAYLOAD_LINE}

# Find and extract the payload in this script.
PAYLOAD_START=$((PAYLOAD_LINE + 1))
echo PAYLOAD_START=${PAYLOAD_START}
tail -n +${PAYLOAD_START} "${SOURCE}" | tar -xzf -

# Pull the latest Docker images from Docker Hub.
docker-compose pull
docker pull hyperledger/fabric-ccenv:x86_64-1.0.0-alpha

# Kill and remove any running Docker containers.
docker-compose -p composer kill
docker-compose -p composer down --remove-orphans

# Kill any other Docker containers.
docker ps -aq | xargs docker rm -f

# Start all Docker containers.
docker-compose -p composer up -d

# Wait for the Docker containers to start and initialize.
sleep 10

# Create the channel on peer0.
docker exec peer0 peer channel create -o orderer0:7050 -c mychannel -f /etc/hyperledger/configtx/mychannel.tx

# Join peer0 to the channel.
docker exec peer0 peer channel join -b mychannel.block

# Fetch the channel block on peer1.
docker exec peer1 peer channel fetch -o orderer0:7050 -c mychannel

# Join peer1 to the channel.
docker exec peer1 peer channel join -b mychannel.block

# Open the playground in a web browser.
case "$(uname)" in 
"Darwin") open http://localhost:8080
          ;;
"Linux")  if [ -n "$BROWSER" ] ; then
	       	        $BROWSER http://localhost:8080
	        elif    which xdg-open > /dev/null ; then
	                xdg-open http://localhost:8080
          elif  	which gnome-open > /dev/null ; then
	                gnome-open http://localhost:8080
          #elif other types blah blah
	        else   
    	            echo "Could not detect web browser to use - please launch Composer Playground URL using your chosen browser ie: <browser executable name> http://localhost:8080 or set your BROWSER variable to the browser launcher in your PATH"
	        fi
          ;;
*)        echo "Playground not launched - this OS is currently not supported "
          ;;
esac

# Exit; this is required as the payload immediately follows.
exit 0
PAYLOAD:
� 'Y �][s���g~�u^��'�~���:�MQAAD�ԩwo����j��e2���=��/��tM�^�[���u�}����xLC�˧ )@����$��x�/(��(�b8A}AP#�/5�s���lm��ڗujo����^+���}$����n����^O�e�!#�r�Stq��_^����W�����*���7������r��dQ\ɿ\(����∽u4\.��������>[ǩ��@�\�$Mӕ���5|��/��t9��{Lĝ��s�=��t��u>�=�p'�4��?��Z�?��4B�����yNٔK�h��(M�Iظ�E����OS$����Q����O�7�g��
?G�?�?E�h�q}l���s�RF�����Nl�jM�>��t��Xk�֩R�.4Q�e$�e���$X`�{+X�R\�ʦ�m�0R�?����_5�
�B ��@���c%�&�'pG���&�OQ4�!
�\gu���=���p:�JH�w��L�	k[�ˋY�E�[�~dK��B]��:5VT�����������M����^��QU�������]����u�G���G��=O�?�T��<�����$�ټ����7�n7�́P֔��'���e�ϚKq�"Zhsa���gݞ�	`ƅ����Y��i1o���h(ō :yNik�����֍��2ġ�i�2n��L���xYCrf�ԙsu0�O�m�w���q'�ǅ������b<j����)1�C�Aɕ��`!��C.��{�p.�������7Me'����\�8�E�୍=w��e�!�Eٔk��������VA�7Ks!?�� �|<4��Z*7V8���ϴ	B�D���MA� ����J��7�v}1ߍ$2%�F�4���5Vl�Z�:����6��Y�J.�\���+-5���Q��Δn�Z�lt�<����\��\�"Of�w�f��y4�P?�����seI
&oE�#]��EY�.2&����N�yX12[�FѦI����(�7͕��e(D�$�8�e�G �C'r����"�.a�E��,��~����a�];�Cn9I[RMM/F]}�d	�,�9�x�,z�4����������5����Ϧ���=����������O}�W�}��Z�������_���Dw��5��0o�w�~�K�[�{�<�CKn�c�0C��q"T�G�z	�B?bԷ*�!)Gv�A,�
�
�]�+�앙{N�� ��,,LCѕ�.�,�`w�8b�N��(��K�T�s԰�D�/['RS�w�йY��f�Ǧ�{E��bn5o;���;P �{-C���O�e�Y�l�S�r!<QuhM���ãr����)gF���E ���@��O�q���1�����8�t�܇�.��w|K3ymJ
�(�Hs?4���\rآQ��R�l�9��L�k|'p$�,���&��/�D�p����t��<�1>�@��亖L��)�fV}ȡ�a��?����k���������?J����g������]���K@�W������/��7�^��D���2�K�������+U�O�����O��$�^@�lqt�"���a��a���]sY��(?pQ2@1g=ҫ��.�!�W���P]�e������qG��V�4��e��,��h\�o����b��Z6�`۶�17�ij��ɗ޲���f�V_r̹�4p�N�#ڃ967�hk�� ����V��(A��f)�Ӱ��y/~I�?3����/%���!*�_���j��������e�?3����R�A�t�#�����-�gz����C��!|��l����:�f��w�бY��{�Ǧ��|h ���N����p\�� ��I��!&��{SinM�	����0w�s��t��$��P�s��m6�7�y�ֻ� 
�4%
��<.t��ʝ��1vL���k̑��#�r68f$�{������[�i��H �B�0$�@ ��7PĖ �!/X��k�N8a7E�j�de:�� �n��;���GӞ=4�*���TQ��w{�χf=�/��$d�����f�u��LiYZw4�C^nvL5QBډI�H�,E�vC2�	\��d�Ѓ������%����?d��e�C����?]�)���*����?�x��w�wov9��Z�)�D����������_�����+��������!���H���U�������t�"�GC�'��]����p�����Q�a	�DX�qX$@H�Ei�$)�����P��/��Ch���2pA��ʄ]�_�V�byñ9�5�f{�9Ҫ�l�m��Rx1�%���q�N+)54$wm'���ǫ{���(ǌ��v��7pD���������=n2�L?�SJN�v�*���x��q���oq���D5�_� ��'߹�;��j��rp��o__f.�?NЕ��������ۗ/���O�8Qɿ|��_Zz��qC�����w���p���O���)���,��E��bD�b�cۤ��K�.F!�K�,f{X�������L8��2��VE���_>��#���������?]D��� �D4L^L�ݠ�nci�x�s�X鮑&����V�p�e���+�au]��S��0"7�3�`��(�|�G�|F�T��Nc�[����&���k��3[��ލ���������#�*�+������S��B���+����/��P&ʐ�+�J�O�	��������>�y����;b%�A�*�5��`����l������п��c�ㆹ��T��bx�T�޺��p#s�m��}�֣}�����߻4��i�M;�L(�|�#�S:E_̋G�Km;��~�HLW���	�<�-b�Zt3����������P�YOԉ5�����j�s�ފ���{�A3Q�t�r֓��ex�L>2�kĠ��Ām���N��-���|�������	+�����Bm���_�SI�;�!qk!̻��!@]�Hr��pY�n���a���&8L9��Ӽ��+.��Sh+��mg#���s�O	+g�O�� r��A 5Ý�i'�I�D��?�}z��Vu}�Ț�Ґ^>�G������������/���O���7�s���U�[�����?[�IS�+���m�G�~�Ǹ0l�^�-��If�����l�G���?�e~��Q~(�b�n!�[ׁ�����<�Z�� 컦�O����~Ѓ!c;9�ݔ����E%qG4�f#�e�\k�-[�)Ѷw�o�T�]K�t*�1I�4�.�S��]K$�_����4^�z�!����8��Х��cw �5�͑��hmւg�.��}{��Y#Yͥ.���d.���j�l��;�j��7|��N�aFHt��*�>=l<����O����� .������J�o�����?��?%�3��������g���������������j�������.0���U��\.��������QE����@���W�����o1�S�g�?�����������1%Q�q(�%\���"��� p���G	�X�
p�G(�����j��
e����G���t5�S
.X���)�rrط̩�f�/0DhN=���l��y�-Z����?� ��q[iXW��E��5��ľ����U8QRs̡��+8��)L�Z:Yg�Q�&���F}���ش������ݹ��%q�_����������t�E���*[?�
+����e~���~���\9j�id����d������B�I��k�1�E\���5re/��}����N�x����"��UM�.�7<�iv��]��W�:��OOL�t�}�ƿh�$����t������ֲI�ʭ�����x׵����q�"�.�ګw~�����������|��y�+�vZ`���7���]y�.�ƋM�?�k��]ݾ��)n�{���K?�Y�Q1*\���bPQ��W�9O+ߕ�2�n�]4��� �~�UQ����7D�.��ߐ��ϋ��Ǿ�WD���e�ZUI��`�;j���y�av}�(�΢��/7�˃����·��śWk���Q��9l/^���o���:b��"{�a�5o�� ��w[/�������Z��_�N�~z5��5x���ߟ_ʾ��?��c��_m�\T{X����q:�.�o�~��q���8��8K]8��'�0Y?P�GR��jO>���D����8���}7@U���?���Y����?6��Wlñ�E��������w�F���Y��{�@�Uő�m�n�ǧM���וU��f9�}�axgkxk�p�Y�gO�:{��O��b����������u���p�\υˡ��2f��{�t]�ҡ"]�n;]�ukO׵ۺ���;1AM�	&����?1�OJ�F��|P	4bD!&������l;g�p� ���=]�^����=���{�G/6�Θ#���ܔ�A	���]!�,�L��H
�FÃ�xA�H2���.%ӑx׭mY;&�:z��"���2�N�q���ʹ�fet,����D�y�'����6!�v1a\ȇm�wTeI�{:884��6�����D�8�����[f&M7�� �Z-�m����L�,��V4]7um����8cg֫���;�$(�>��Lv�C�-d�4E��q	�
_e;R���,�7�h}0�׌3�U�=4��H�̪��5Π˰Ѳ��f�����!�PZ����YG�W�VE��{���Y^�M��h�|��t��o�t�M�)r8]�ɆSN���98�������trb��;�G�_'W�A���EEb�*wZ�E�}��\���c��j���e�u���_�2�&�t*i��H�s�ou�C֑�����sFO�T�x֜��H�2�4ҹ�+�5�o��2�+Q�5N�)F�`t������Շ��|m]p\r����T��2��c����M�z�b����\4([�(E������@]�9�Z�+7��9��ˑ~�S{����|U2�-�
�ÍF1���|vϹ����$�B��ɏ9)�:b!Cf<�Kp���:��2b�f�mN�4��޴��tx��Kǧ�}�a�+#��U{I6ta-*���B��.dy��>G����89���U�t?�}��C�y�(�sy�`�؟O>��{��w������Qc������������'�J<
k'6N�=���~~��ZK%.<��T�~��U�O	������ߏ��m/ˣ����U�.}T�|����á����Cp��ޡ���_���/��z��j�'�M��~�ң��
���n�OP�Ϝ��q ��z�N v��Ћwn����8|���s��f����� glj�oj��/n
sF�>����,n���^T�t����X��6z	c�yN�=W���D��� C�w~�����2]�a���f��#��!xv�e��l�(�ϐn7�/D�����
m�Y@�����50_����N��4�/�(������}N6s��x� �8���dC�`)Ly����~��0�DW�h5����tp�#\�ǔ� �O�ɂ}v{k�P&���Lc3U\dB*�=�z6ߣ�x���
�SՖ���KAI�VIUe�e�!>��RjJ��^o���,��#�AO����6�f®��>a�C�R�"lxjSOa�M=�!�5;��f{��Ss%dݪ�8���Z4]S�7������@&)m���v���e����_�D� �˓����=������b&Lf�p>!� a��!+�;LI�S0ӂ� jG2,���	xY��Ȏ��!+މl�� ���x�UR�e�����b9!�)_��b5͋��8P2���*�F���\5�L��v"^ ��0����1&��6�ﾲ��IY�l�:�,[��f�; �\��v8���p�w'�f�C�W{Z�3\+�;�p�EZ�J(Ɗ��ؤ��8%��J��reeP�m�pʂ3�׊�ÅTP4M��<�iI�9e�#<#�TY����}4�����ct��ʁ��a����YO8ޤE1�e0�sޝ����[HP�����5�r�W�&ݾXB�sU����VYb����@Y��+K}e����P�U��(I��,�8@��Н:C���.`^د�y���Pø-�[;�vbX)W�x��A�a$&5����)KXL֤� �{Q5P�A�aR�t��1Ȝ����]�,Lh�}��b�Z�J� ӽA�����BΛM����`�͆����d_k�\���>O����lRd�d�p{��N��OY���f�l�϶�϶q
7~���Z�W5�#й�+�h:��=�v����Wi+�����C��}*�:tf:����U�y�r��6���6'!v�ӆn��W����f���*o~J�R�@7A7�p��6.I��w���QL!�km�f~�Р����'�3|K�hف�C��)pR�%�q�k��djC�:��~�9���;��tZ�s:�v&��OX2�A��g�Z�l9tt�.|��8�ʳ�͒����<�e�@ϝ�qE��bT�3硗!#�bF���Dhq���4�	� \mF���O������y��Q���u�u�o���/�K��/�8t�LZ�-�P�J+�@��ny��s
Ǿ𠥎�H[�h,:Ύs��hP���V]p��g��������U��4��5e��;��D�O���"�ΐڔ�"�U=lD�"��L )ni��H��H4�D@?T�H�+$�B���bJy4�;��:�Ը��^+4�1U94��JO��b�zχ�HPH���iL���qs��Q�!b�X�&i�"���3��Vu�[�!F3��4b#}i>���'���_z[
*�8� -�Ź��������@�� ߧz1!��-�B�<��Z�Y�4`�:���Y����RM+��1��8��q��a�~�k�K��Mw*o�r.O���1��1�V́X��=���S��N[v}މ��>��r��u��]����{X���aّ�N$m�A$3a�p%����%�h�lN�T>����`1�ʃ��\�y�zP�	�Lb"�A�I��((2ݬ)��O�����;��`�6�&����"�� I�pDl�4Nm��Q:(L��h�Ea���.	7B��tpY�ÃA��ӥ��P1!-LC��B�a�!������� ��r����&ߎzv8��R�'�D]�B�����G�/����b�|T�_�J�
���`�<���,�@�~6�}�+[r����kbbK����,�k�z)�v�I�U��Haߥ�W��l㰍���8���g#~+9e�C;e39V}��[!̶q�j��Bl����Xk	�����Ռ��GO7L���#�x����kz�� ����Z�����P\d��&Ak�T&�������+w��Q�&���$t��rs/����#�}s����F藿��S����/������8t��kv���{�w��lZ8Q���8���z��������˒�s����Ƀt��7N���_�n<�@���y�__|���?=�^<߉?�u�J��~erEO�ym4��V�6��o�?�'?����n���󯁗����Г��x���HA��v���ޜ�v�jS;mj�M��i6M��v�_q��_q퀴����6�Ӧv�>��������[^F>�C�*W����Y�=�rAl�נ�B'��zl��c&�N�����/�C�MQ^�l�����<�S��)����a�{�38G����Af���צ��,��93v�՞3cO���sfl㰍�2̙9�|�#L��3s.w���Ui��.y�ɜ��/�:h\1�g';��Nvzߦ�����  