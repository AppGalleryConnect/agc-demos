import agconnect from '@agconnect/api';
import '@agconnect/instance';

/**
 * Initializes app configuration
 */

export function configInstance(){
  agconnect.instance().configInstance(agConnectConfig);
}

var agConnectConfig =
  {
    // Fill in the code when integrating AGC development services into your Web application,
    // the code can be obtained in your project on the AGC portal.
  }

