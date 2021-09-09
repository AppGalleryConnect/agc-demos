/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License")
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import { StyleSheet } from "react-native";

export default styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: "#e1e1e1",
  },
  container: {
    flex: 1,
    backgroundColor: "#e1e1e1",
    padding: 10
  },
  phoneInputContainer: {
    width: '100%',
    height: 60,
    flexDirection: 'row',
    paddingVertical: 10,
  },
  linkContainer: {
    width: '100%',
    justifyContent: "center",
    flexDirection: "row"
  },
  link: {
    color: "blue"
  },
  infoContainer: {
    flexDirection: 'row',
    paddingVertical: 10,
  },
})